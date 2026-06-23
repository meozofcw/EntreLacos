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
     * e retorna a URL pública com cache-buster, ou null em caso de erro.
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

            // Nome de arquivo único por upload — evita qualquer cache de CDN/Coil
            val fileName = "$uid/${UUID.randomUUID()}.jpg"

            SupabaseClient.client.storage
                .from(bucket)
                .upload(fileName, bytes) {
                    upsert = true
                }

            val publicUrl = SupabaseClient.client.storage
                .from(bucket)
                .publicUrl(fileName)

            // Cache-buster: garante que o Coil sempre trate como uma imagem nova
            "$publicUrl?t=${System.currentTimeMillis()}"

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}