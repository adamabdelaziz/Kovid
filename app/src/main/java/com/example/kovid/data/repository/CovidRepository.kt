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
}