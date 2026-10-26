package com.partners.hdfils_agent.domain.models

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

enum class DeviseFilter { ALL, DOLLAR, FC }

enum class DateFilter { ALL, TODAY, YESTERDAY, FIVE_DAYS_AGO, THIS_MONTH }

private val KINSHASA: TimeZone = TimeZone.getTimeZone("Africa/Kinshasa")

fun kinshasaNowStamp(nowMillis: Long = System.currentTimeMillis()): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    format.timeZone = KINSHASA
    return format.format(Date(nowMillis))
}

fun parseClientInstant(raw: String): Long? {
    val text = raw.trim()
    if (text.isEmpty()) return null
    val dateTime = Regex("""^(\d{4}-\d{2}-\d{2})[T ](\d{2}:\d{2}:\d{2})(?:\.\d+)?(Z|[+-]\d{2}:\d{2})?$""")
        .matchEntire(text)
    if (dateTime != null) {
        val base = "${dateTime.groupValues[1]}T${dateTime.groupValues[2]}"
        val zone = dateTime.groupValues[3]
        return when {
            zone.isEmpty() -> parseZoned(base, "yyyy-MM-dd'T'HH:mm:ss", KINSHASA)
            zone == "Z" -> parseZoned(base, "yyyy-MM-dd'T'HH:mm:ss", TimeZone.getTimeZone("UTC"))
            else -> parseZoned(base + zone, "yyyy-MM-dd'T'HH:mm:ssXXX", TimeZone.getTimeZone("UTC"))
        }
    }
    val dateOnly = Regex("""^(\d{4}-\d{2}-\d{2})$""").matchEntire(text) ?: return null
    return parseZoned(dateOnly.groupValues[1], "yyyy-MM-dd", KINSHASA)
}

fun Client.matchesSearch(query: String): Boolean {
    val tokens = query.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (tokens.isEmpty()) return true
    val haystack = listOf(nom, prenom, displayName(), avenue, quartier, numero_parcelle)
    return tokens.all { token -> haystack.any { it.contains(token, ignoreCase = true) } }
}

fun Client.matchesDevise(filter: DeviseFilter): Boolean {
    if (filter == DeviseFilter.ALL) return true
    val value = devise.trim().uppercase(Locale.US)
    return when (filter) {
        DeviseFilter.DOLLAR -> value == "$" || value == "USD" || value == "US$" || value == "DOLLAR"
        DeviseFilter.FC -> value == "FC" || value == "CDF"
        DeviseFilter.ALL -> true
    }
}

fun Client.matchesDate(filter: DateFilter, nowMillis: Long = System.currentTimeMillis()): Boolean {
    if (filter == DateFilter.ALL) return true
    val instant = created_at?.let(::parseClientInstant) ?: return false
    val now = calendarAt(nowMillis)
    val created = calendarAt(instant)
    return when (filter) {
        DateFilter.TODAY -> sameDay(created, now)
        DateFilter.YESTERDAY -> sameDay(created, shiftDays(now, -1))
        DateFilter.FIVE_DAYS_AGO -> sameDay(created, shiftDays(now, -5))
        DateFilter.THIS_MONTH ->
            created.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                created.get(Calendar.MONTH) == now.get(Calendar.MONTH)
        DateFilter.ALL -> true
    }
}

fun mergeCreatedAt(incoming: List<Client>, existing: List<Client>): List<Client> {
    val known = existing.mapNotNull { client ->
        client.created_at?.takeIf { it.isNotBlank() }?.let { client.identityKey() to it }
    }.toMap()
    return incoming.map { client ->
        if (!client.created_at.isNullOrBlank()) client
        else client.copy(created_at = known[client.identityKey()])
    }
}

private fun parseZoned(text: String, pattern: String, zone: TimeZone): Long? {
    return try {
        val format = SimpleDateFormat(pattern, Locale.US)
        format.timeZone = zone
        format.isLenient = false
        val position = ParsePosition(0)
        val date = format.parse(text, position) ?: return null
        if (position.index != text.length) null else date.time
    } catch (_: Exception) {
        null
    }
}

private fun calendarAt(millis: Long): Calendar {
    return Calendar.getInstance(KINSHASA).apply { timeInMillis = millis }
}

private fun shiftDays(source: Calendar, days: Int): Calendar {
    return (source.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, days)
    }
}

private fun sameDay(left: Calendar, right: Calendar): Boolean {
    return left.get(Calendar.YEAR) == right.get(Calendar.YEAR) &&
        left.get(Calendar.DAY_OF_YEAR) == right.get(Calendar.DAY_OF_YEAR)
}
