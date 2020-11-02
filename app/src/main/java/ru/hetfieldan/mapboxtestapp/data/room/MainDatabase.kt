package ru.hetfieldan.mapboxtestapp.data.room


import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(
    entities = [
        CarsTable::class
    ],
    version = 4,
    exportSchema = false
)

abstract class MainDatabase: RoomDatabase() {
    abstract val carsDAO: CarsDAO
}
