package com.partners.hdfils_agent.data.utils

object AgentCodeValidator {
    private val allowed = Regex("^[A-Za-z0-9._-]+$")

    fun errorOrNull(raw: String): String? {
        val code = raw.trim()
        return when {
            code.isEmpty() -> "Le code agent est obligatoire"
            code.length < 3 -> "Le code agent doit contenir au moins 3 caractères"
            code.length > 32 -> "Le code agent est trop long"
            !allowed.matches(code) -> "Utilisez des lettres, des chiffres, un point, un tiret ou un underscore"
            else -> null
        }
    }
}
