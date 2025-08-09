package com.hexo.tools.location

import android.content.Context
import com.hexo.core.location.LocationUtils
import com.hexo.tools.AssistantTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * LocationTool retrieves the device's last known location and returns it
 * as a human-readable string. It requires location permissions to be
 * granted to the app. The returned location can be used by other tools
 * such as WeatherTool.
 */
class LocationTool(private val context: Context) : AssistantTool {
    override val name: String = "location"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        return withContext(Dispatchers.IO) {
            val location = LocationUtils.getLastKnownLocation(context)
            if (location != null) {
                "Your last known location is lat=${location.latitude}, lon=${location.longitude}."
            } else {
                "I couldn't determine your location. Please ensure location services are enabled."
            }
        }
    }
}