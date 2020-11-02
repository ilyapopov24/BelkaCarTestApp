package ru.hetfieldan.mapboxtestapp.data.datasource

import io.reactivex.Observable
import io.reactivex.Single
import ru.hetfieldan.mapboxtestapp.data.room.CarsDAO
import ru.hetfieldan.mapboxtestapp.data.room.CarsTable
import ru.hetfieldan.mapboxtestapp.domain.CarItem
import ru.hetfieldan.mapboxtestapp.domain.CarsMapper
import javax.inject.Inject

class DBDataSource @Inject constructor(
    private val dao: CarsDAO,
    private val carsMapper: CarsMapper
) {
    fun cacheItems(items: List<CarItem>): List<Long> {
        val timeNow = System.currentTimeMillis()
        val carsTables = carsMapper
            .mapCarsItemsToTables(items)
            .map {
                it.copy(cacheTime = timeNow)
            }
        return dao.refreshItems(carsTables)
    }

    fun getItems(): Single<List<CarItem>> = dao.getItems().map { carsMapper.mapCarsTablesToItems(it) }
}
