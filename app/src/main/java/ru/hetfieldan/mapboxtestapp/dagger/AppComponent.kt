package ru.hetfieldan.mapboxtestapp.dagger

import android.app.Application
import android.support.v7.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import ru.hetfieldan.mapboxtestapp.dagger.modules.*
import ru.hetfieldan.mapboxtestapp.presentation.ui.MainActivity
import javax.inject.Singleton


@Singleton
@Component(modules = [
    NetworkModule::class,
    RoomModule::class,
    ViewModelModule::class,
    BindingModule::class,
    MapboxMapModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindApplication(app: Application): Builder

        @BindsInstance
        fun bindActivity(activity: AppCompatActivity): Builder
    }

    fun injectMainActivity(activity: MainActivity): MainActivity
}
