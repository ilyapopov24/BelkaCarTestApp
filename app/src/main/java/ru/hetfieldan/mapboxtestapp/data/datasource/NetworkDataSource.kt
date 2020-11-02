package ru.hetfieldan.mapboxtestapp.data.datasource

import io.reactivex.Single
import ru.hetfieldan.mapboxtestapp.data.network.CarsAPI
import ru.hetfieldan.mapboxtestapp.domain.CarItem
import javax.inject.Inject

class NetworkDataSource @Inject constructor(private val api: CarsAPI) {
    fun getCars(): Single<List<CarItem>> = api.getCars()
}
