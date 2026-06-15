package com.entrelacos.arandu.model

data class SensoryContent(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",        // "video" ou "activity"
    val category: String = "",    // "comunicacao", "emocoes", "sensorial", "cognitivo"
    val youtubeId: String = "",   // ID do vídeo no YouTube (só para type == "video")
    val activityContent: String = "", // Texto/instrução da atividade
    val ageRange: String = "",    // Ex: "3-6", "6-10"
    val rating: Double = 0.0,
    val ratingCount: Int = 0,
    val createdAt: String = ""
)

enum class SensoryCategory(val key: String, val label: String, val emoji: String) {
    ALL("", "Tudo", "✨"),
    COMUNICACAO("comunicacao", "Comunicação", "💬"),
    EMOCOES("emocoes", "Emoções", "💛"),
    SENSORIAL("sensorial", "Sensorial", "🎨"),
    COGNITIVO("cognitivo", "Cognitivo", "🧩")
}