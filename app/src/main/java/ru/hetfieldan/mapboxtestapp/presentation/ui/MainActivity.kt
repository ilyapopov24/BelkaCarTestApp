package ru.hetfieldan.mapboxtestapp.presentation.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProvider
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.hetfieldan.mapboxtestapp.*
import ru.hetfieldan.mapboxtestapp.dagger.DaggerAppComponent
import ru.hetfieldan.mapboxtestapp.domain.*
import ru.hetfieldan.mapboxtestapp.viewmodels.CarsViewModel
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private var currentMapboxMap: MapboxMap? = null
    private var userLocation: Point? = null
    private var compositeDisposable = CompositeDisposable()
    private var carsFeatures: List<Feature> = ArrayList()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CarsViewModel

    @Inject lateinit var mainOptions: MapboxMapOptions
    @Inject lateinit var mainLayer: SymbolLayer
    @Inject lateinit var locationOptions: LocationComponentOptions
    @Inject lateinit var permissionsManager: PermissionsManager
    @Inject lateinit var directionsLayer: LineLayer
    @Inject lateinit var geoJsonSource: GeoJsonSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAppComponent.builder()
            .bindApplication(application)
            .bindActivity(this)
            .build()
            .injectMainActivity(this)

        viewModel = getViewModel(viewModelFactory)
        initMap(savedInstanceState)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (allPermissionsGranted(grantResults)) {
            initMapFragment(null)
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
            showSettingsIntentDialog(getString(R.string.user_location_permission_explanation))
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        currentMapboxMap = mapboxMap
        initIcons()
        currentMapboxMap?.addLayer(mainLayer)
        showCarsOnMap()
        makeLocationComponent()?.let {
            userLocation = getUserLocation(it)
        }
        initDirectionsSourceAndLayer()
        currentMapboxMap?.addOnMapClickListener(this@MainActivity)
    }

    override fun onMapClick(latLng: LatLng) {
        val pointf: PointF = currentMapboxMap?.projection?.toScreenLocation(latLng) ?: return
        val rectF = RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10)
        currentMapboxMap?.let { mapboxMap ->
            val featureList: List<Feature> = mapboxMap.queryRenderedFeatures(rectF, MAIN_LAYER_ID)
            if (featureList.isNotEmpty()) {
                for (feature in featureList) {
                    val carJSON = feature.getStringProperty(PROPERTY_CAR_JSON)
                    val car = Gson().fromJson(carJSON, CarItem::class.java)
                    SymbolLayerBottomSheet.newInstance(carJSON)
                        .show(supportFragmentManager, SymbolLayerBottomSheet::class.java.simpleName)
                    showRouteFromUserLocation(Point.fromLngLat(car.longitude, car.latitude))
                }
            }
        }
    }

    private fun initMap(savedInstanceState: Bundle?) {
        viewModel.getCars()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { cars -> handleCarsList(cars, savedInstanceState) }
            .addTo(compositeDisposable)
    }

    private fun handleCarsList(cars: List<CarItem>, savedInstanceState: Bundle?) {
        carsFeatures = viewModel.mapCarsToFeatures(cars)
        if (permissionsManager.locationPermissionNotGranted()) {
            permissionsManager.requestLocationPermission()
        } else {
            initMapFragment(savedInstanceState)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMapFragment(savedInstanceState: Bundle?) {
        val mapFragment = if (savedInstanceState == null) makeNewMapFragment() else findExistingMapFragment()
        mapFragment.getMapAsync(this)
    }

    private fun makeNewMapFragment(): SupportMapFragment {
        val mapFragment = SupportMapFragment.newInstance(mainOptions)
        supportFragmentManager.beginTransaction()
            .add(R.id.mainContainer, mapFragment, "com.mapbox.map")
            .commit()
        return mapFragment
    }

    private fun findExistingMapFragment(): SupportMapFragment {
        return supportFragmentManager.findFragmentByTag("com.mapbox.map") as SupportMapFragment
    }

    private fun initIcons() {
        val blackIcon = BitmapFactory.decodeResource(this@MainActivity.resources, R.drawable.car_silhouette_black)
        val blueIcon = BitmapFactory.decodeResource(this@MainActivity.resources, R.drawable.car_silhouette_blue)
        currentMapboxMap?.addImage(BlackColor::class.java.simpleName, blackIcon)
        currentMapboxMap?.addImage(BlueColor::class.java.simpleName, blueIcon)
    }

    private fun showCarsOnMap() {
        val geoJsonSource = makeSourceFromCars()
        currentMapboxMap?.addSource(geoJsonSource)
    }

    private fun makeSourceFromCars(): GeoJsonSource {
        return GeoJsonSource(
            MAIN_SOURCE_ID,
            FeatureCollection.fromFeatures(carsFeatures)
        )
    }

    @SuppressLint("MissingPermission")
    private fun makeLocationComponent(): LocationComponent? {
        return currentMapboxMap?.locationComponent?.apply {
            activateLocationComponent(this@MainActivity, locationOptions)
            isLocationComponentEnabled = true
            cameraMode = CameraMode.TRACKING
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation(locationComponent: LocationComponent): Point? {
        val lastKnownLocation = locationComponent.lastKnownLocation ?: return null
        val userLng = lastKnownLocation.longitude
        val userLat = lastKnownLocation.latitude
        return Point.fromLngLat(userLng, userLat)
    }

    private fun initDirectionsSourceAndLayer() {
        currentMapboxMap?.addSource(geoJsonSource)
        currentMapboxMap?.addLayerBelow(directionsLayer, MAIN_LAYER_ID)
    }

    private fun showRouteFromUserLocation(destination: Point) {
        userLocation?.let {
            val client = viewModel.makeDirectionsClient(it, destination, getString(R.string.mapbox_access_token))

            viewModel.getRouteFromMapbox(client)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resp ->
                    resp.body()?.let { respBody ->
                        val routesGeometry = respBody.routes()[0]
                        val directionsCollection = viewModel.makeDirectionsCollection(routesGeometry)
                        val directionsSource: GeoJsonSource? = currentMapboxMap?.getSourceAs(DIRECTIONS_SOURCE_ID)
                        directionsSource?.setGeoJson(directionsCollection)
                    }
                }
                .addTo(compositeDisposable)
        }
    }
}
