package ru.hetfieldan.mapboxtestapp.domain


import com.google.gson.annotations.SerializedName

data class CarItem(
    val id: Int,

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
