package com.example.kovid.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kovid.data.entities.StateMetadata
import com.example.kovid.data.entities.StateValue
import com.example.kovid.data.entities.USValue

@Dao
interface CovidDao {

    //Do this class

    @Query("SELECT * FROM USValue")
    fun getCurrentUSValues(): LiveData<USValue>

    //change red ID to the variable name in CovidItem
    @Query("SELECT * FROM StateValues WHERE state = :state")
    fun getCurrentStateValues(state: String): LiveData<List<StateValue>>

    @Query("SELECT * FROM StateValues")
    fun getAllStateValues(): LiveData<List<StateValue>>

    @Query("SELECT * FROM StateMetadata")
    fun getStateMetadata(): LiveData<List<StateMetadata>>

    @Query("SELECT * FROM StateValues WHERE state =:state")
    fun getHistoricValuesForAState(state: String): LiveData<List<StateValue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoricValuesForAState(
        listOfStateValue: List<StateValue>
    )
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStateValues(listOfStateValue: List<StateValue>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneStateValue(stateValue: List<StateValue>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUSValues(currentUSValues: USValue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStateMetadata(stateMetadata: List<StateMetadata>)

}