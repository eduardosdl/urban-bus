package com.exemple.urbanbus.utils

fun calculateMinutesDifference(currentHour: String, arrivalHour: String): Int {
    val currentHourEmMinutos = convertToMinutes(currentHour)
    val arrivalHourEmMinutos = convertToMinutes(arrivalHour)
    val diferencaEmMinutos = arrivalHourEmMinutos - currentHourEmMinutos

    return if (diferencaEmMinutos <= 59) diferencaEmMinutos else -1
}

fun convertToMinutes(horario: String): Int {
    val horas = horario.substring(0, 2).toInt()
    val minutos = horario.substring(3, 5).toInt()

    return horas * 60 + minutos
}
