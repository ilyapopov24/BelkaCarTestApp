package ru.hetfieldan.mapboxtestapp.domain.usecase

import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import io.reactivex.Observable
import retrofit2.Response
import ru.hetfieldan.mapboxtestapp.domain.repo.RoutesRepo
import javax.inject.Inject


class DrawRouteUseCaseImpl @Inject constructor(private val routesRepo: RoutesRepo) : DrawRouteUseCase {

    override fun makeDirectionsCollection(route: DirectionsRoute): FeatureCollection? {
        return routesRepo.makeDirectionsCollection(route)
    }

    override fun getRouteFromMapbox(client: MapboxDirections): Observable<Response<DirectionsResponse>> {
        return routesRepo.getRouteFromMapbox(client)
    }

    override fun makeDirectionsClient(origin: Point, destination: Point, token: String): MapboxDirections {
        return routesRepo.makeDirectionsClient(origin, destination, token)
    }
}
