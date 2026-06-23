package com.entrelacos.arandu.repository

import android.content.Context
import android.net.Uri
import com.entrelacos.arandu.data.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class StorageRepository {

    /**
     * Faz upload de uma imagem da galeria (Uri local) para o Supabase Storage
     * e retorna a URL pública, ou null em caso de erro.
     *
     * @param bucket "avatars" ou "posts"
     */
    suspend fun uploadImage(
        context: Context,
        uri: Uri,
        bucket: String,
        uid: String
    ): String? = withContext(Dispatchers.IO) {
        try {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return@withContext null

            val fileName = "$uid/${UUID.randomUUID()}.jpg"

            SupabaseClient.client.storage
                .from(bucket)
                .upload(fileName, bytes) {
                    upsert = true
                }

            SupabaseClient.client.storage
                .from(bucket)
                .publicUrl(fileName)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}