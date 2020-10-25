package com.example.kovid.data.remote

import com.example.kovid.data.entities.StateMetadata
import com.example.kovid.data.entities.StateValue
import com.example.kovid.data.entities.USValue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidService {

    //Could be Covids or CovidItem in <>
    @GET("/v1/us/current.json")
    suspend fun getCurrentUSValues(): Response<USValue>

    @GET("/v1/states/current.json")
    suspend fun getAllStateValues(): Response<List<StateValue>>

    //will probably have to make this Response<List<StateValue>>
    //or not?
    @GET("/v1/states/{state}/current.json")
    suspend fun getCurrentStateValues(
        @Path(
            "state",
            encoded = true
        ) state: String
    ): Response<List<StateValue>>

    @GET("/v1/states/{state}/{date}.json")
    suspend fun getCurrentValueForAStateOnADate(
        @Path("state") state: String,
        @Path("date") date: String
    ): Response<StateValue>

    @GET("/v1/states/{state}/{date}.json")
    suspend fun getCurrentUSValuesOnADate(@Path("date") date: String): Response<USValue>


    @GET("/v1/states/info.json")
    suspend fun getStateMetadata(): Response<List<StateMetadata>>

    @GET("/v1/states/{state}/current.json")
    suspend fun getCurrentValuesForAState(@Path("state") state: String): Response<List<StateValue>>

    @GET("/v1/states/{state}/daily.json")
    suspend fun getHistoricValuesForAState(@Path("state") state: String): Response<List<StateValue>>
}