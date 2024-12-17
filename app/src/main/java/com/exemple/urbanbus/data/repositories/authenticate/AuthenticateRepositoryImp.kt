package com.exemple.urbanbus.data.repositories.authenticate

import android.util.Log
import com.exemple.urbanbus.BuildConfig
import com.exemple.urbanbus.data.api.OlhoVivoAPI

class AuthenticateRepositoryImp(
    private val olhoVivoAPI: OlhoVivoAPI
) : AuthenticateRepository {
    // repository para request de autenticacao necessaria para buscar os cookies e realizar outras chamadas
    override suspend fun authenticate(): String {
        var cookie: String = ""
        try {
            val token = BuildConfig.API_TOKEN
            val request = olhoVivoAPI.authenticate(token)
            cookie = request.headers().values("Set-Cookie").joinToString(";")
        } catch (e: Exception) {
            Log.e("test", e.toString())
        }
        return cookie
    }
}