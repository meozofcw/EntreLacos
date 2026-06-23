package com.entrelacos.arandu.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://yebawnviqlecaxzgmsrw.supabase.co",
        supabaseKey = "sb_publishable_kjkeyYftIonzT1o7lGFEdQ_vpUy9_ZB"
    ) {
        install(Storage)
    }
}