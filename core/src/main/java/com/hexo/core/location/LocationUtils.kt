package com.hexo.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Utilities for retrieving device location. In a real implementation you
 * should use Google Play Services FusedLocationProviderClient for more
 * accurate results and handle runtime permission requests. Here we simply
 * query the last known location from the LocationManager.
 */
object LocationUtils {
    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(context: Context): Location? = withContext(Dispatchers.IO) {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = manager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = try {
                manager.getLastKnownLocation(provider)
            } catch (e: SecurityException) {
                null
            }
            if (l != null && (bestLocation == null || l.accuracy < bestLocation!!.accuracy)) {
                bestLocation = l
            }
        }
        bestLocation
    }
}