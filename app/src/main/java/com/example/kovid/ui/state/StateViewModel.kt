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

    private val _state = _id.switchMap { id->
        repository.getCurrentValueForAState(id)
    }

    val state : LiveData<Resource<List<StateValue>>> = _state

    fun getState(id: String) {
        Timber.d(id + " VM ")
        Timber.d("getState in VM called")
        //val newID = id.toLowerCase()
        repository.getCurrentValueForAState(id)
        Timber.d("getState in VM finished?")
    }
}