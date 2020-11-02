package ru.hetfieldan.mapboxtestapp.dagger.modules

import dagger.Binds
import dagger.Module
import ru.hetfieldan.mapboxtestapp.data.repo.CarsRepoImpl
import ru.hetfieldan.mapboxtestapp.data.repo.RoutesRepoImpl
import ru.hetfieldan.mapboxtestapp.domain.repo.CarsRepo
import ru.hetfieldan.mapboxtestapp.domain.repo.RoutesRepo
import ru.hetfieldan.mapboxtestapp.domain.usecase.CarsUseCase
import ru.hetfieldan.mapboxtestapp.domain.usecase.CarsUseCaseImpl
import ru.hetfieldan.mapboxtestapp.domain.usecase.DrawRouteUseCase
import ru.hetfieldan.mapboxtestapp.domain.usecase.DrawRouteUseCaseImpl

@Module
abstract class BindingModule {
    @Binds
    abstract fun bindCarsUseCase(carsUseCase: CarsUseCaseImpl): CarsUseCase

    @Binds
    abstract fun bindDrawRouteUseCase(drawRouteUseCase: DrawRouteUseCaseImpl): DrawRouteUseCase

    @Binds
    abstract fun bindCarsRepo(carsRepo: CarsRepoImpl): CarsRepo

    @Binds
    abstract fun bindRoutesRepo(routesRepo: RoutesRepoImpl): RoutesRepo
}
