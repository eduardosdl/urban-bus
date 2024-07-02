package com.exemple.urbanbus.data.repositories.stops

import android.util.Log
import com.exemple.urbanbus.data.api.OlhoVivoAPI
import com.exemple.urbanbus.data.models.BusStop
import com.exemple.urbanbus.data.models.BusStopLineArrival
import com.exemple.urbanbus.data.repositories.authenticate.AuthenticateRepository
import com.exemple.urbanbus.utils.UiState

class BusStopRepositoryImp(
    private val authRepository: AuthenticateRepository,
    private val olhoVivoAPI: OlhoVivoAPI
) : BusStopRepository {
    override suspend fun getBusStops(searchTerm: String, result: (UiState<List<BusStop>>) -> Unit) {
        val busStops = mutableListOf<BusStop>()
        try {
            busStops.addAll(olhoVivoAPI.getBusStops(authRepository.authenticate(), searchTerm))
            result.invoke(UiState.Success(busStops))
        } catch (e: Exception) {
            Log.d("app-error-stops", "Error to get all stops: $e")
            result.invoke(UiState.Failure("Houve um erro ao buscar paradas"))
        }
    }

    override suspend fun getLineArrival(
        busStopCode: String,
        result: (UiState<List<BusStopLineArrival>>) -> Unit
    ) {
        try {
            val stopArrival = olhoVivoAPI.getStopArrival(authRepository.authenticate(), busStopCode)

            val busLineArrival: List<BusStopLineArrival> = stopArrival.busStopData.busLineArrival.map { line ->
                if (line.direction == 1) {
                    line.copy(destination = line.mainTerminal, currentHour = stopArrival.hour)
                } else {
                    line.copy(destination = line.secondaryTerminal, currentHour = stopArrival.hour)
                }
            }
            result.invoke(UiState.Success(busLineArrival))

            Log.d("test", "BusStopRepositoryImp: $busLineArrival")
        } catch (e: Exception) {
            Log.d("app-error-stops", "Error to get stops forecast: $e")
            result.invoke(UiState.Failure("Houve um erro ao buscar prvisões"))
        }
    }
}