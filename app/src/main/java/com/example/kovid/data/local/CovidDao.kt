package com.example.kovid.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kovid.data.entities.StateValue
import com.example.kovid.data.entities.USValues

@Dao
interface CovidDao {

    //Do this class

    @Query("SELECT * FROM USValues")
    fun getCurrentUSValues() : LiveData<USValues>

    //change red ID to the variable name in CovidItem
    @Query("SELECT * FROM StateValues WHERE state = :state")
    fun getCurrentStateValues(state: String): LiveData<StateValue>

    @Query("SELECT * FROM StateValues")
    fun getAllStateValues(): LiveData<List<StateValue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStateValues(listOfStateValue: List<StateValue>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOneStateValue(stateValue: StateValue)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentUSValues(currentUSValues: USValues)

}