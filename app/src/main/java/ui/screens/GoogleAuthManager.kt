package com.entrelacos.arandu.auth

import kotlinx.coroutines.tasks.await
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthManager(
    private val context: Context
) {

    private val auth = FirebaseAuth.getInstance()

    suspend fun signIn(): Boolean {

        val googleIdOption =
            GetGoogleIdOption.Builder()
                .setServerClientId(
                    "725615842237-9f40t646boqk2pc6so2igne0vgem43ur.apps.googleusercontent.com"
                )
                .setFilterByAuthorizedAccounts(false)
                .build()

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

        val credentialManager =
            CredentialManager.create(context)

        val result =
            credentialManager.getCredential(
                context = context,
                request = request
            )

        val credential = result.credential

        val googleCredential =
            GoogleIdTokenCredential.createFrom(
                credential.data
            )

        val firebaseCredential =
            GoogleAuthProvider.getCredential(
                googleCredential.idToken,
                null
            )

        auth.signInWithCredential(firebaseCredential)
            .await()

        return auth.currentUser != null
    }
}