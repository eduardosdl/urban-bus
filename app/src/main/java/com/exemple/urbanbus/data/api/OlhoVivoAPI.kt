package com.exemple.urbanbus.data.api

import com.exemple.urbanbus.data.dtos.BusStopArrivalDTO
import com.exemple.urbanbus.data.models.BusLine
import com.exemple.urbanbus.data.models.BusStop
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface OlhoVivoAPI {
    @POST("Login/Autenticar")
    suspend fun authenticate(@Query("token") token: String): Response<String>

    @GET("Parada/Buscar")
    suspend fun getBusStops(
        @Header("Cookie") cookie: String,
        @Query("termosBusca") term: String = ""
    ): List<BusStop>

    @GET("Previsao/Parada")
    suspend fun getStopArrival(
        @Header("Cookie") cookie: String,
        @Query("codigoParada") busStopCode: String = ""
    ): BusStopArrivalDTO

    @GET("Linha/Buscar")
    suspend fun getBusLines(
        @Header("Cookie") cookie: String,
        @Query("termosBusca") busStopCode: String
    ): List<BusLine>
}