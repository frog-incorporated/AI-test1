package com.hexo.tools.weather

import com.hexo.core.location.LocationUtils
import com.hexo.tools.AssistantTool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WeatherTool fetches weather information either by coordinates or by city
 * name. Without an API key it will return stubbed data. Users can supply
 * an optional API key via settings to call a real weather service.
 */
class WeatherTool : AssistantTool {
    override val name: String = "weather"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        // Example input: "What is the weather in Jacksonville?"
        // This stub just returns a fixed weather description.
        return withContext(Dispatchers.IO) {
            "It's sunny and 25°C."
        }
    }
}