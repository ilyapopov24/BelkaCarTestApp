package ru.hetfieldan.mapboxtestapp.dagger.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.hetfieldan.mapboxtestapp.dagger.keys.ViewModelKey
import ru.hetfieldan.mapboxtestapp.viewmodels.CarsViewModel
import ru.hetfieldan.mapboxtestapp.viewmodels.ViewModelFactory


@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CarsViewModel::class)
    abstract fun bindCarsViewModel(viewModel: CarsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
