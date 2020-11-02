package ru.hetfieldan.mapboxtestapp.data.repo

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.hetfieldan.mapboxtestapp.addTo
import ru.hetfieldan.mapboxtestapp.data.datasource.DBDataSource
import ru.hetfieldan.mapboxtestapp.data.datasource.NetworkDataSource
import ru.hetfieldan.mapboxtestapp.domain.CarItem
import ru.hetfieldan.mapboxtestapp.domain.repo.CarsRepo
import ru.hetfieldan.mapboxtestapp.mylog
import javax.inject.Inject

class CarsRepoImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dbDataSource: DBDataSource
): CarsRepo {

    override fun getCars(compositeDisposable: CompositeDisposable): Observable<List<CarItem>> {
        return dbDataSource.getItems()
            .subscribeOn(Schedulers.io())
            .doOnError { error -> error.printStackTrace() }
            .toObservable()
            .concatMap {
                val cacheMaxTime = System.currentTimeMillis() * 1000 * 60 * 60
                val cacheTime = if (it.isNotEmpty()) it[0].cacheTime ?: 0 else 0
                if (it.isEmpty() || cacheTime > cacheMaxTime) getCarsFromNetwork(compositeDisposable)
                else Observable.fromArray(it)
            }
    }

    private fun getCarsFromNetwork(compositeDisposable: CompositeDisposable): Observable<List<CarItem>>? {
        return networkDataSource.getCars()
            .subscribeOn(Schedulers.io())
            .map {
                cacheCars(it, compositeDisposable)
                it
            }
            .onErrorReturn { listOf() }
            .toObservable()
    }

    override fun cacheCars(items: List<CarItem>, compositeDisposable: CompositeDisposable) {
        Observable.fromCallable { dbDataSource.cacheItems(items) }
            .subscribeOn(Schedulers.io())
            .doOnError { error -> error.printStackTrace() }
            .subscribe()
            .addTo(compositeDisposable)
    }
}
