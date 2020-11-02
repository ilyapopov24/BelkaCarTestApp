package ru.hetfieldan.mapboxtestapp.data.repo

import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.hetfieldan.mapboxtestapp.domain.repo.RoutesRepo
import ru.hetfieldan.mapboxtestapp.mylog
import javax.inject.Inject

class RoutesRepoImpl @Inject constructor() : RoutesRepo {

    override fun getRouteFromMapbox(client: MapboxDirections): Observable<Response<DirectionsResponse>> {
        return Observable.fromCallable { client.executeCall() }
            .subscribeOn(Schedulers.io())
            .onErrorReturn {
                Response.success(null)
            }
    }

    override fun makeDirectionsCollection(route: DirectionsRoute): FeatureCollection? {
        val geometry = route.geometry() ?: return null
        val directionsRouteFeatureList: MutableList<Feature> = ArrayList()
        val lineString = LineString.fromPolyline(geometry, Constants.PRECISION_6)
        val coordinates = lineString.coordinates()
        for (i in coordinates.indices) {
            directionsRouteFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(coordinates)))
        }
        return FeatureCollection.fromFeatures(directionsRouteFeatureList)
    }

    override fun makeDirectionsClient(origin: Point, destination: Point, token: String): MapboxDirections = MapboxDirections.builder()
        .origin(origin)
        .destination(destination)
        .overview(DirectionsCriteria.OVERVIEW_FULL)
        .profile(DirectionsCriteria.PROFILE_WALKING)
        .accessToken(token)
        .build()
}
