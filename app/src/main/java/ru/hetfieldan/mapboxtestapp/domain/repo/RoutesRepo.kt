package ru.hetfieldan.mapboxtestapp.domain.repo

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

interface RoutesRepo {
    fun getRouteFromMapbox(client: MapboxDirections): Observable<Response<DirectionsResponse>>
    fun makeDirectionsCollection(route: DirectionsRoute): FeatureCollection?
    fun makeDirectionsClient(origin: Point, destination: Point, token: String): MapboxDirections
}
