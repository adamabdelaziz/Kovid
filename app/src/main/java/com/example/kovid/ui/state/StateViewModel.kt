package com.example.kovid.ui.state

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kovid.data.repository.CovidRepository

class StateViewModel @ViewModelInject constructor(private val repository: CovidRepository) :
    ViewModel() {

    val stateMetadata = repository.getStateMetaData()

}