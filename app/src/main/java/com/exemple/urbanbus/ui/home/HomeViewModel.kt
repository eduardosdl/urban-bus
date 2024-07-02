package com.exemple.urbanbus.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemple.urbanbus.data.models.BusStop
import com.exemple.urbanbus.data.repositories.authenticate.AuthenticateRepository
import com.exemple.urbanbus.data.repositories.stops.BusStopRepository
import com.exemple.urbanbus.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticateRepository: AuthenticateRepository,
    private val busStopRepository: BusStopRepository
) : ViewModel() {
    private val _stops = MutableLiveData<UiState<List<BusStop>>>()
    val stops: MutableLiveData<UiState<List<BusStop>>> = _stops

    fun authenticate() = viewModelScope.launch {
        val authenticated = authenticateRepository.authenticate()
        Log.d("test", "view model data: $authenticated")
    }

    fun getAllBusStops() = viewModelScope.launch {
        _stops.value = UiState.Loading
        busStopRepository.getBusStops {
            _stops.value = it
            if (it is UiState.Success) {
                Log.d("test", "view model data: ${it.data}")
            }
        }
    }

}