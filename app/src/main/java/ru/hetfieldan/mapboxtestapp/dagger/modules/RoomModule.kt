package ru.hetfieldan.mapboxtestapp.dagger.modules

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import ru.hetfieldan.mapboxtestapp.BasicApplication
import ru.hetfieldan.mapboxtestapp.data.room.CarsDAO
import ru.hetfieldan.mapboxtestapp.data.room.MainDatabase
import javax.inject.Singleton


@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideDB(context: Application): MainDatabase =
        Room.databaseBuilder(
            context,
            MainDatabase::class.java,
            "main_database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideCarsDAO(db: MainDatabase): CarsDAO = db.carsDAO
}
