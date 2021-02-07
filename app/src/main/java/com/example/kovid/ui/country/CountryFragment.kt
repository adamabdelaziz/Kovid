package com.example.kovid.ui.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kovid.data.entities.USValue
import com.example.kovid.databinding.FragmentCountryBinding
import com.example.kovid.utils.Resource
import com.yabu.livechart.model.DataPoint
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //call fragment setup functions here
        setupCurrentUSValues()
        setupHistoricUSValues()
    }

    private fun setupCurrentUSValues() {
        viewModel.currentUSValues.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("SUCCESS")
                    val data = it.data

                    if (data != null) {
                        Timber.d("binding data")
                        bindData(data)
                    }
                }
                Resource.Status.ERROR -> {
                    Timber.d("ERROR")
                }
                Resource.Status.LOADING -> {
                    Timber.d("LOADING")
                }
            }
        })
    }

    private fun setupHistoricUSValues() {
        viewModel.historicUSValues.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("SUCCESS")
                    binding.constraintLayout.visibility = View.VISIBLE

                    //refer to StateFragment equivalent if the array size bug occurs again
                    val list = it.data
                    if (list != null) {
                        Timber.d("Historic USValue list not null")
                        setupCharts(list)
                    }

                }
                Resource.Status.ERROR -> {
                    Timber.d("ERROR")
                }
                Resource.Status.LOADING -> {
                    Timber.d("LOADING")
                    binding.constraintLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun setupCharts(historicUSValues: List<USValue>) {
        Timber.d("setup chart called")

        val liveChart = binding.liveChart
        val liveChartTwo = binding.liveChartTwo
        val liveChartThree = binding.liveChartThree

        val positives = mutableListOf<DataPoint>()
        val hospitializations = mutableListOf<DataPoint>()
        val deaths = mutableListOf<DataPoint>()

        positives.clear()
        hospitializations.clear()
        deaths.clear()
    }

    private fun bindData(USValue: USValue) {
        Timber.d("bindData called")
        binding.usValue = USValue
    }
}

//comment purgatory, delete later

//        viewModel.stateMetadata.observe(viewLifecycleOwner, {
//            when (it.status) {
//                Resource.Status.SUCCESS -> {
//                    Timber.d("SUCCESS")
//                    for (i in it.data!!){
//                        Timber.d(i.name)
//                    }
//                }
//                Resource.Status.ERROR -> {
//                    Timber.d("ERROR")
//                }
//                Resource.Status.LOADING -> {
//                    Timber.d("LOADING")
//                }
//            }
//        })