package ru.hetfieldan.mapboxtestapp.domain

import com.google.gson.Gson
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import ru.hetfieldan.mapboxtestapp.data.room.CarsTable
import ru.hetfieldan.mapboxtestapp.presentation.ui.MainActivity
import javax.inject.Inject

class CarsMapper @Inject constructor() {

    fun mapCarsItemsToTables(cars: List<CarItem>): List<CarsTable> {
        return cars.map {
            Gson().fromJson(Gson().toJson(it), CarsTable::class.java)
        }
    }

    fun mapCarsTablesToItems(cars: List<CarsTable>): List<CarItem> {
        return cars.map {
            Gson().fromJson(Gson().toJson(it), CarItem::class.java)
        }
    }

    fun mapCarsToFeatures(carsList: List<CarItem>): List<Feature> {
        return carsList.map { car ->
            Feature.fromGeometry(
                Point.fromLngLat(car.longitude, car.latitude)
            ).apply {
                addNumberProperty(PROPERTY_ANGLE, car.angle)
                addStringProperty(PROPERTY_COLOR, getCarColor(car.color)::class.java.simpleName)
                addStringProperty(PROPERTY_CAR_JSON, Gson().toJson(car))
            }
        }
    }

    private fun getCarColor(colorStr: String): CarsColors {
        return when (colorStr) {
            "black" -> BlackColor
            else -> BlueColor
        }
    }
}
