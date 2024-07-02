package com.exemple.urbanbus.utils

import com.exemple.urbanbus.data.models.BusStop

fun getBusStopTitle(busStop: BusStop): String {
    return busStop.name.ifBlank {
        busStop.code.toString()
    }
}