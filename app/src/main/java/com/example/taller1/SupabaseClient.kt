package com.example.taller1

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://zqmhjbkihfqgzhlebltb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpxbWhqYmtpaGZxZ3pobGVibHRiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzU2MDA1NTEsImV4cCI6MjA5MTE3NjU1MX0._1RnpABG3kR0yYkd97sebAHfd4wO3XXZbKtsWIULt9o"
    ){
        install(Postgrest)
        install(Auth)
    }
}