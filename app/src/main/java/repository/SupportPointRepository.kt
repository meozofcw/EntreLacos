package com.entrelacos.arandu.repository

import com.entrelacos.arandu.model.SupportPoint
import com.google.firebase.firestore.FirebaseFirestore

class SupportPointRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("support_points")

    fun getSupportPoints(onResult: (List<SupportPoint>) -> Unit) {
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            val points = snapshot.documents.mapNotNull { doc ->
                try {
                    SupportPoint(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        category = doc.getString("category") ?: ""
                    )
                } catch (e: Exception) {
                    null
                }
            }

            onResult(points)
        }
    }
}