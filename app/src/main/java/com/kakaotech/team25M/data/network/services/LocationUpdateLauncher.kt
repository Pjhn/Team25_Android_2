package com.kakaotech.team25M.data.network.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.kakaotech.team25M.ui.status.utils.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationUpdateLauncher @Inject constructor(
    val context: Context,
    private val locationManager: LocationManager
) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val INTERVAL_MILLIS = 5_000L

    @SuppressLint("MissingPermission")
    fun startLocationUpdate() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_MILLIS)
                .build()

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
            .addOnFailureListener { fail ->
                Log.w("LocationService", "Failed Message: ${fail.localizedMessage}")
            }
    }

    fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            lastLocation?.let {
                Log.i(
                    "LocationService", "longitude: ${lastLocation.longitude}\n" + "" +
                        "latitude: ${lastLocation.latitude}"
                )
                CoroutineScope(Dispatchers.IO).launch{
                    locationManager.storeLocation(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            }
        }
    }
}
