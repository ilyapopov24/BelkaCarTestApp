package ru.hetfieldan.mapboxtestapp.domain.usecase

import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import io.reactivex.Observable
import retrofit2.Response
import ru.hetfieldan.mapboxtestapp.R

interface DrawRouteUseCase {
    fun makeDirectionsCollection(route: DirectionsRoute): FeatureCollection?
    fun getRouteFromMapbox(client: MapboxDirections): Observable<Response<DirectionsResponse>>
    fun makeDirectionsClient(origin: Point, destination: Point, token: String): MapboxDirections
}
