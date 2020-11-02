package ru.hetfieldan.mapboxtestapp.data.room


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import io.reactivex.Single

@Dao
interface CarsDAO {
    companion object {
        const val TABLE_NAME = "cars_table"
    }

    @Insert
    fun insertItems(items: List<CarsTable>): List<Long>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getItems(): Single<List<CarsTable>>

    @Query("DELETE FROM $TABLE_NAME")
    fun clear()

    @Transaction
    fun refreshItems(items: List<CarsTable>): List<Long> {
        clear()
        return insertItems(items)
    }
}