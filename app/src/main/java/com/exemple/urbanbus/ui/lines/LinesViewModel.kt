package com.exemple.urbanbus.ui.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exemple.urbanbus.data.models.BusLine
import com.exemple.urbanbus.data.repositories.lines.BusLineRepository
import com.exemple.urbanbus.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinesViewModel @Inject constructor(
    private val linesRepository: BusLineRepository
) : ViewModel() {
    private val _lines = MutableLiveData<UiState<List<BusLine>>>()
    val lines: LiveData<UiState<List<BusLine>>> get() = _lines

    fun getLines(serchTerm: String) = viewModelScope.launch {
        linesRepository.getBusLines(serchTerm) {
            _lines.value = it
        }
    }
}