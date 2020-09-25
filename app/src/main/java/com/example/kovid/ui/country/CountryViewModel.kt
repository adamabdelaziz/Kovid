package com.example.kovid.ui.country

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.kovid.data.entities.USValue
import com.example.kovid.data.repository.CovidRepository

class CountryViewModel @ViewModelInject constructor(private val repository: CovidRepository) :
    ViewModel() {

    val USValue = repository.getCurrentUSValues()
}