package ru.hetfieldan.mapboxtestapp.viewmodels

import android.arch.lifecycle.ViewModel
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response
import ru.hetfieldan.mapboxtestapp.domain.CarItem
import ru.hetfieldan.mapboxtestapp.domain.usecase.DrawRouteUseCase
import ru.hetfieldan.mapboxtestapp.domain.usecase.CarsUseCase
import javax.inject.Inject

class CarsViewModel @Inject constructor(
    private val carsUseCase: CarsUseCase,
    private val drawRouteUseCase: DrawRouteUseCase
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getCars(): Observable<List<CarItem>> = carsUseCase.getCars(compositeDisposable)
    fun mapCarsToFeatures(carsList: List<CarItem>): List<Feature> = carsUseCase.mapCarsToFeatures(carsList)
    fun makeDirectionsCollection(route: DirectionsRoute): FeatureCollection? = drawRouteUseCase.makeDirectionsCollection(route)
    fun getRouteFromMapbox(client: MapboxDirections): Observable<Response<DirectionsResponse>> = drawRouteUseCase.getRouteFromMapbox(client)
    fun makeDirectionsClient(origin: Point, destination: Point, token: String) = drawRouteUseCase.makeDirectionsClient(origin, destination, token)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
