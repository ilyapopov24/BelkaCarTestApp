package ru.hetfieldan.mapboxtestapp.domain.usecase

import com.mapbox.geojson.Feature
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.hetfieldan.mapboxtestapp.domain.*
import ru.hetfieldan.mapboxtestapp.domain.repo.CarsRepo
import javax.inject.Inject

class CarsUseCaseImpl @Inject constructor(
    private val carsRepo: CarsRepo,
    private val carsMapper: CarsMapper
): CarsUseCase {

    override fun getCars(compositeDisposable: CompositeDisposable): Observable<List<CarItem>> {
        return carsRepo.getCars(compositeDisposable)
    }

    override fun mapCarsToFeatures(carsList: List<CarItem>): List<Feature> {
        return carsMapper.mapCarsToFeatures(carsList)
    }
}
