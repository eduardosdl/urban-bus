package com.exemple.urbanbus.data.repositories.stops

import com.exemple.urbanbus.data.models.BusStopLineArrival
import com.exemple.urbanbus.data.models.BusStop
import com.exemple.urbanbus.utils.UiState

interface BusStopRepository {
    suspend fun getBusStops(searchTerm: String = "", result: (UiState<List<BusStop>>) -> Unit)
    suspend fun getLineArrival(busStopCode: String, result: (UiState<List<BusStopLineArrival>>) -> Unit)
}