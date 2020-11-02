package ru.hetfieldan.mapboxtestapp.domain.usecase

import com.mapbox.geojson.Feature
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.hetfieldan.mapboxtestapp.domain.CarItem

interface CarsUseCase {
    fun getCars(compositeDisposable: CompositeDisposable): Observable<List<CarItem>>
    fun mapCarsToFeatures(carsList: List<CarItem>): List<Feature>
}
