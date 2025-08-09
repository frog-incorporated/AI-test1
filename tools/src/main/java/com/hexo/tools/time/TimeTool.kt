package com.hexo.tools.time

import com.hexo.tools.AssistantTool
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * TimeTool provides the current time for a given city or timezone. It
 * interprets simple phrases like "What time is it in London?" and maps
 * them to a Java timezone. The mapping is naive; for production use, a
 * comprehensive geocoding service should be integrated.
 */
class TimeTool : AssistantTool {
    override val name: String = "time"

    override suspend fun handle(input: String, extras: Map<String, Any>): String {
        // Attempt to extract a city name from the input using a simple regex
        val city = Regex("in ([A-Za-z\s]+)").find(input)?.groupValues?.getOrNull(1)?.trim()
        val zoneId = when (city?.lowercase()) {
            null, "here", "my location" -> ZoneId.systemDefault()
            "london" -> ZoneId.of("Europe/London")
            "jacksonville" -> ZoneId.of("America/New_York")
            else -> ZoneId.systemDefault()
        }
        val time = ZonedDateTime.now(zoneId)
        val formatted = time.format(DateTimeFormatter.ofPattern("HH:mm, MMM d yyyy"))
        return "The current time in ${city ?: "your location"} is $formatted"
    }
}