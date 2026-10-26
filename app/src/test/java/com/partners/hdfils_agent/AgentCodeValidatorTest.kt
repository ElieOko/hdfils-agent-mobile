package com.partners.hdfils_agent

import com.partners.hdfils_agent.data.utils.AgentCodeValidator
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
}
