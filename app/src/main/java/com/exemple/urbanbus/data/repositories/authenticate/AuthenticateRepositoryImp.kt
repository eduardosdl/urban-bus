package com.exemple.urbanbus.data.repositories.authenticate

import android.util.Log
import com.exemple.urbanbus.data.api.OlhoVivoAPI

class AuthenticateRepositoryImp(
    private val olhoVivoAPI: OlhoVivoAPI
) : AuthenticateRepository {
    override suspend fun authenticate(): String {
        var cookie: String = ""
        try {
            val token = ""
            val request = olhoVivoAPI.authenticate(token)
            cookie = request.headers().values("Set-Cookie").joinToString(";")
        } catch (e: Exception) {
            Log.e("test", e.toString())
        }
        return cookie
    }
}