package ru.hetfieldan.mapboxtestapp.data.room

//import androidx.room.Entity
//import androidx.room.PrimaryKey
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "cars_table")
data class CarsTable(
    @PrimaryKey
    val id: Int = 0,

    val cacheTime: Long?,

    val angle: Int,
    val color: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,

    @SerializedName("fuel_percentage")
    val fuelPercentage: Int,

    @SerializedName("plate_number")
    val plateNumber: String
)
