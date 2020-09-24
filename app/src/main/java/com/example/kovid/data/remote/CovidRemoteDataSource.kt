package com.example.kovid.data.remote

import javax.inject.Inject

class CovidRemoteDataSource @Inject constructor(private val covidService: CovidService) : BaseDataSource() {

    suspend fun getCurrentUSValues() = getResult {covidService.getCurrentUSValues()}
    suspend fun getCurrentValueForAState(state : String) = getResult{covidService.getCurrentValueForAState(state)}
    suspend fun getCurrentValuesForAllStates() = getResult { covidService.getCurrentValuesForAllStates() }
}