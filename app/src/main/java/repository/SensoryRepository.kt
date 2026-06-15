package com.entrelacos.arandu.repository

import com.entrelacos.arandu.model.SensoryContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class SensoryRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("sensory_content")

    fun getContent(onResult: (List<SensoryContent>) -> Unit) {
        collection
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val items = snapshot.documents.mapNotNull { doc ->
                    try {
                        SensoryContent(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            type = doc.getString("type") ?: "",
                            category = doc.getString("category") ?: "",
                            youtubeId = doc.getString("youtubeId") ?: "",
                            activityContent = doc.getString("activityContent") ?: "",
                            ageRange = doc.getString("ageRange") ?: "",
                            rating = doc.getDouble("rating") ?: 0.0,
                            ratingCount = (doc.getLong("ratingCount") ?: 0L).toInt()
                        )
                    } catch (e: Exception) { null }
                }
                onResult(items)
            }
    }

    fun rateContent(contentId: String, stars: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ratingRef = collection.document(contentId)
            .collection("ratings").document(uid)

        ratingRef.set(mapOf("stars" to stars)).addOnSuccessListener {
            // Recalcula média
            collection.document(contentId)
                .collection("ratings")
                .get()
                .addOnSuccessListener { ratings ->
                    val allStars = ratings.documents.mapNotNull { it.getLong("stars")?.toInt() }
                    if (allStars.isNotEmpty()) {
                        val avg = allStars.average()
                        collection.document(contentId).update(
                            mapOf(
                                "rating" to avg,
                                "ratingCount" to allStars.size
                            )
                        )
                    }
                }
        }
    }
}