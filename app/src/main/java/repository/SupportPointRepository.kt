package com.entrelacos.arandu.repository

import com.entrelacos.arandu.model.SupportPoint
import com.google.firebase.firestore.FirebaseFirestore

class SupportPointRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("support_points")

    fun getSupportPoints(onResult: (List<SupportPoint>) -> Unit) {
        collection
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val points = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(SupportPoint::class.java)?.copy(id = doc.id)
                }
                onResult(points)
            }
    }
}
