package com.example.kovid.data.remote

import com.example.kovid.data.entities.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidService {

    //Could be Covids or CovidItem in <>
    @GET("/v1/us/current.json")
    suspend fun getCurrentUSValues() : Response<USValues>

    @GET("/v1/states/{state}/current.json")
    suspend fun getCurrentValueForAState(@Path("state") state :String):Response<StateValue>

    @GET("/v1/states/current.json")
    suspend fun getCurrentValuesForAllStates():Response<List<StateValue>>
}