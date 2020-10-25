package com.example.kovid.data.repository

import com.example.kovid.data.local.CovidDao
import com.example.kovid.data.remote.CovidRemoteDataSource
import com.example.kovid.utils.performGetOperation
import javax.inject.Inject


class CovidRepository @Inject constructor(
    private val remoteDataSource: CovidRemoteDataSource,
    private val localDataSource: CovidDao
) {

    //Actually do this class
    fun getCurrentValuesForAState(state:String)= performGetOperation(
        databaseQuery = {localDataSource.getCurrentValuesForAState(state)},
        networkCall = {remoteDataSource.getCurrentValuesForAState(state)},
        saveCallResult = {localDataSource.insertCurrentValuesForAState(it)}

    )

    fun getCurrentUSValues() = performGetOperation(
        databaseQuery = { localDataSource.getCurrentUSValues() },
        networkCall = { remoteDataSource.getCurrentUSValues() },
        saveCallResult = { localDataSource.insertCurrentUSValues(it) }
    )

    fun getStateMetaData() = performGetOperation(
        databaseQuery = { localDataSource.getStateMetadata() },
        networkCall = { remoteDataSource.getStateMetadata() },
        saveCallResult = { localDataSource.insertStateMetadata(it) }
    )

    fun getCurrentValueForAState(state: String) = performGetOperation(
        databaseQuery = { localDataSource.getCurrentStateValues(state) },
        networkCall = { remoteDataSource.getCurrentStateValues(state) },
        saveCallResult = { localDataSource.insertOneStateValue(it) }
    )

    fun getAllStateValues() = performGetOperation(
        databaseQuery = { localDataSource.getAllStateValues() },
        networkCall = { remoteDataSource.getAllStateValues() },
        saveCallResult = { localDataSource.insertAllStateValues(it) }
    )

    fun getHistoricValuesForAState(state: String) = performGetOperation(
        databaseQuery = {localDataSource.getHistoricValuesForAState(state)},
        networkCall = {remoteDataSource.getHistoricValuesForAState(state)},
        saveCallResult = {localDataSource.insertHistoricValuesForAState(it)}
    )
}