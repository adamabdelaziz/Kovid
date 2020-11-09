package com.example.kovid.data.remote

import javax.inject.Inject

class CovidRemoteDataSource @Inject constructor(private val covidService: CovidService) :
    BaseDataSource() {

    suspend fun getCurrentUSValues() = getResult { covidService.getCurrentUSValues() }
    suspend fun getHistoricUSValues() = getResult { covidService.getHistoricUSValues() }

    suspend fun getCurrentStateValues(state: String) =
        getResult { covidService.getCurrentStateValues(state) }

    //suspend fun getAllStateValues() = getResult { covidService.getAllStateValues() }
    suspend fun getStateMetadata() = getResult { covidService.getStateMetadata() }
    suspend fun getHistoricValuesForAState(state: String) =
        getResult { covidService.getHistoricValuesForAState(state) }

    suspend fun getCurrentValuesForAState(state : String) = getResult { covidService.getCurrentValuesForAState(state) }
}