package ru.hetfieldan.mapboxtestapp.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import ru.hetfieldan.mapboxtestapp.domain.PERMISSION_REQUEST_LOCATION
import javax.inject.Inject

class PermissionsManager @Inject constructor(private val activity: AppCompatActivity) {
    fun locationPermissionNotGranted(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
    }
}
