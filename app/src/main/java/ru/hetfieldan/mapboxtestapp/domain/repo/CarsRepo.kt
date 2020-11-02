package ru.hetfieldan.mapboxtestapp.domain.repo

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ru.hetfieldan.mapboxtestapp.domain.CarItem

interface CarsRepo {
    fun getCars(compositeDisposable: CompositeDisposable): Observable<List<CarItem>>
    fun cacheCars(items: List<CarItem>, compositeDisposable: CompositeDisposable)
}
