package com.exemple.urbanbus.data.repositories.lines

import android.util.Log
import com.exemple.urbanbus.data.api.OlhoVivoAPI
import com.exemple.urbanbus.data.models.BusLine
import com.exemple.urbanbus.data.repositories.authenticate.AuthenticateRepository
import com.exemple.urbanbus.utils.UiState

class BusLineRepositoryImp (
    private val authRepository: AuthenticateRepository,
    private val olhoVivoAPI: OlhoVivoAPI
) : BusLineRepository {
    override suspend fun getBusLines(busStopCode: String, result: (UiState<List<BusLine>>) -> Unit) {
        try {
            val response = olhoVivoAPI.getBusLines(authRepository.authenticate(), busStopCode)
            result.invoke(UiState.Success(response))
        } catch (e: Exception) {
            Log.d("app-error-stops", "Error to get lines: $e")
        }
    }
}