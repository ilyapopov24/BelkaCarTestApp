package ru.hetfieldan.mapboxtestapp.data.network

import io.reactivex.Single
import retrofit2.http.*
import ru.hetfieldan.mapboxtestapp.domain.CarItem

interface CarsAPI {
    @GET("Gary111/TrashCan/master/2000_cars.json")
    fun getCars(): Single<List<CarItem>>
}
