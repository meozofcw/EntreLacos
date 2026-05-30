package com.entrelacos.arandu.repository

import com.entrelacos.arandu.model.Post
import com.google.firebase.firestore.FirebaseFirestore

class PostRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getPosts(
        onResult: (List<Post>) -> Unit
    ) {

        db.collection("posts")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, _ ->

                if (snapshot == null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val posts = snapshot.documents.mapNotNull {

                    it.toObject(Post::class.java)?.copy(
                        id = it.id
                    )
                }

                onResult(posts.reversed())
            }
    }
}