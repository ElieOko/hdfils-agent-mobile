package com.partners.hdfils_agent

import com.partners.hdfils_agent.domain.models.Client
import com.partners.hdfils_agent.domain.models.DateFilter
import com.partners.hdfils_agent.domain.models.DeviseFilter
import com.partners.hdfils_agent.domain.models.matchesDate
import com.partners.hdfils_agent.domain.models.matchesDevise
import com.partners.hdfils_agent.domain.models.matchesSearch
import com.partners.hdfils_agent.domain.models.mergeCreatedAt
import com.partners.hdfils_agent.domain.models.parseClientInstant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ClientFilterTest {
    private val now = parseClientInstant("2026-10-26T15:00:00")!!

    @Test
    fun todayYesterdayAndFiveDaysUseKinshasaCalendarDays() {
        val today = Client(nom = "A", created_at = "2026-10-26T08:00:00")
        val yesterday = Client(nom = "A", created_at = "2026-10-25T23:30:00")
        val five = Client(nom = "A", created_at = "2026-10-21T00:05:00")
        val dayAfterFive = Client(nom = "A", created_at = "2026-10-22T00:00:00")
        assertTrue(today.matchesDate(DateFilter.TODAY, now))
        assertFalse(today.matchesDate(DateFilter.YESTERDAY, now))
        assertTrue(yesterday.matchesDate(DateFilter.YESTERDAY, now))
        assertTrue(five.matchesDate(DateFilter.FIVE_DAYS_AGO, now))
        assertFalse(dayAfterFive.matchesDate(DateFilter.FIVE_DAYS_AGO, now))
    }

    @Test
    fun currentMonthIncludesOnlyThisMonth() {
        assertTrue(Client(created_at = "2026-10-01T00:00:00").matchesDate(DateFilter.THIS_MONTH, now))
        assertFalse(Client(created_at = "2026-09-30T23:00:00").matchesDate(DateFilter.THIS_MONTH, now))
    }

    @Test
    fun missingDateStaysOnAllOnly() {
        val client = Client(nom = "Sans date")
        assertFalse(client.matchesDate(DateFilter.TODAY, now))
        assertTrue(client.matchesDate(DateFilter.ALL, now))
    }

    @Test
    fun dollarAndFrancAliases() {
        assertTrue(Client(devise = "$").matchesDevise(DeviseFilter.DOLLAR))
        assertTrue(Client(devise = "USD").matchesDevise(DeviseFilter.DOLLAR))
        assertTrue(Client(devise = "FC").matchesDevise(DeviseFilter.FC))
        assertTrue(Client(devise = "cdf").matchesDevise(DeviseFilter.FC))
        assertFalse(Client(devise = "FC").matchesDevise(DeviseFilter.DOLLAR))
    }

    @Test
    fun searchMatchesNameAvenueQuarterAndNumber() {
        val client = Client(
            nom = "Oko",
            prenom = "Elie",
            avenue = "Londala",
            quartier = "Ngunda Lokombe",
            numero_parcelle = "16"
        )
        assertTrue(client.matchesSearch("oko"))
        assertTrue(client.matchesSearch("elie londala"))
        assertTrue(client.matchesSearch("ngunda 16"))
        assertFalse(client.matchesSearch("masina"))
    }

    @Test
    fun mergeKeepsLocalCreatedAtWhenServerOmitsIt() {
        val local = Client(
            nom = "Oko",
            prenom = "Elie",
            telephone = "0827824163",
            created_at = "2026-10-26T10:00:00"
        )
        val merged = mergeCreatedAt(listOf(local.copy(created_at = null)), listOf(local))
        assertEquals("2026-10-26T10:00:00", merged.single().created_at)
    }

    @Test
    fun utcStampIsParsed() {
        assertNotNull(parseClientInstant("2026-10-26T14:00:00Z"))
        assertNotNull(parseClientInstant("2026-10-26T15:00:00+01:00"))
    }
}
