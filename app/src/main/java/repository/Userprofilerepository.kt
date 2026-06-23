package com.entrelacos.arandu.repository

import com.entrelacos.arandu.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user_profiles")

    fun getProfile(onResult: (UserProfile?) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(null)

        collection.document(uid).addSnapshotListener { doc, error ->
            if (error != null || doc == null || !doc.exists()) {
                onResult(null)
                return@addSnapshotListener
            }
            try {
                val profile = UserProfile(
                    uid = uid,
                    name = doc.getString("name") ?: "",
                    photoUrl = doc.getString("photoUrl") ?: "",
                    bio = doc.getString("bio") ?: "",
                    city = doc.getString("city") ?: "",
                    notificationsEnabled = doc.getBoolean("notificationsEnabled") ?: true,
                    childName = doc.getString("childName") ?: "",
                    childAge = doc.getString("childAge") ?: "",
                    childCondition = doc.getString("childCondition") ?: ""
                )
                onResult(profile)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun saveProfile(profile: UserProfile, onComplete: (Boolean) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return onComplete(false)

        val data = mapOf(
            "name" to profile.name,
            "photoUrl" to profile.photoUrl,
            "bio" to profile.bio,
            "city" to profile.city,
            "notificationsEnabled" to profile.notificationsEnabled,
            "childName" to profile.childName,
            "childAge" to profile.childAge,
            "childCondition" to profile.childCondition
        )

        collection.document(uid).set(data)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun updatePhotoUrl(photoUrl: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        collection.document(uid).set(
            mapOf("photoUrl" to photoUrl),
            com.google.firebase.firestore.SetOptions.merge()
        )
    }

    fun updateNotifications(enabled: Boolean) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        collection.document(uid).update("notificationsEnabled", enabled)
    }
}