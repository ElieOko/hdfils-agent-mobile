package com.partners.hdfils_agent

import com.partners.hdfils_agent.data.utils.AgentCodeValidator
import com.partners.hdfils_agent.domain.models.ClientCreate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AgentCodeValidatorTest {
    @Test
    fun blankCodeIsRejected() {
        assertEquals("Le code agent est obligatoire", AgentCodeValidator.errorOrNull("   "))
    }

    @Test
    fun shortCodeIsRejected() {
        assertEquals(
            "Le code agent doit contenir au moins 3 caractères",
            AgentCodeValidator.errorOrNull("ab")
        )
    }

    @Test
    fun symbolsAreRejected() {
        assertEquals(
            "Utilisez des lettres, des chiffres, un point, un tiret ou un underscore",
            AgentCodeValidator.errorOrNull("ag ent!")
        )
    }

    @Test
    fun validCodePasses() {
        assertNull(AgentCodeValidator.errorOrNull("  AG-12_ok  "))
    }

    @Test
    fun clientCreateMatchesApiPayload() {
        val json = Json { encodeDefaults = false }
        val payload = json.encodeToString(
            ClientCreate(
                nom = "Oko",
                prenom = "Elie",
                telephone = "0827824163",
                commune_id = 1,
                avenue = "londala",
                quartier = "ngunda lokombe",
                numero_parcelle = "16",
                montant_souscription = 24000,
                devise = "FC",
                nombre_tranches = 4
            )
        )
        assertEquals(
            """{"nom":"Oko","prenom":"Elie","telephone":"0827824163","commune_id":1,"avenue":"londala","quartier":"ngunda lokombe","numero_parcelle":"16","montant_souscription":24000,"devise":"FC","nombre_tranches":4}""",
            payload
        )
    }
}
