package com.example.kovid.ui.state

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.kovid.data.entities.StateValue
import com.example.kovid.data.repository.CovidRepository
import com.example.kovid.utils.Resource
import timber.log.Timber


class StateViewModel @ViewModelInject constructor(private val repository: CovidRepository) :
    ViewModel() {


        val stateMetadata = repository.getStateMetaData()
        val US = repository.getCurrentUSValues()
        val states = repository.getAllStateValues()




    private val _id = MutableLiveData<String>()

    private val _state = _id.switchMap { id ->
        repository.getCurrentValueForAState(id)
    }

    val state: LiveData<Resource<List<StateValue>>> = _state

    fun getHistoricData(state: String): LiveData<Resource<List<StateValue>>> {
        return repository.getHistoricValuesForAState(state)
    }
    fun getCurrentData(state:String): LiveData<Resource<StateValue>>{
        return repository.getCurrentValuesForAState(state)
    }

    fun getState(id: String) {
        Timber.d(id + " VM ")
        Timber.d("getState in VM called")
        //val newID = id.toLowerCase()
//        repository.getCurrentValueForAState(id)
//        repository.getHistoricValuesForAState(id)
        Timber.d("getState in VM finished?")
    }
}