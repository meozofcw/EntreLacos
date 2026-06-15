package com.entrelacos.arandu.model

data class SupportPoint(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val category: String = ""
)

enum class SupportCategory(val key: String, val label: String, val emoji: String) {
    ALL("", "Todos", "📍"),
    DELEGACIA("delegacia", "Delegacias", "🚔"),
    ASSISTENCIA("assistencia_social", "Assistência", "🤝"),
    SAUDE("saude", "Saúde", "🏥"),
    JURIDICO("juridico", "Jurídico", "⚖️"),
    ONG("ong", "ONGs", "💛")
}