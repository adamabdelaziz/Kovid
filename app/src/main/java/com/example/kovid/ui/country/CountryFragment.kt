package com.example.kovid.ui.country

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kovid.R
import com.example.kovid.data.entities.USValue
import com.example.kovid.databinding.FragmentCountryBinding
import com.example.kovid.utils.Resource
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChart
import com.yabu.livechart.view.LiveChartStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_state.*
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
        val hospitalizations = mutableListOf<DataPoint>()
        val deaths = mutableListOf<DataPoint>()

        positives.clear()
        hospitalizations.clear()
        deaths.clear()

        //reversed() puts it in the correct order chronologically
        //API doesnt require reversed() for historicUS data
        //drop(n-90) gives the past 90 days only
        val usList = historicUSValues.drop(historicUSValues.size - 89)

        for ((index, value) in usList.withIndex()) {

            if (value.positive != null && value.totalTestResults != null && value.hospitalizedCurrently != null && value.death != null) {
                val dataPointPos = DataPoint(index.toFloat(), value.positive.toFloat())
                val dataPointHosp =
                    DataPoint(index.toFloat(), value.hospitalizedCurrently.toFloat())
                val dataPointDeath = DataPoint(index.toFloat(), value.death.toFloat())
                Timber.d(index.toString() + " TRUE INDEX??")

                positives.add(dataPointPos)
                hospitalizations.add(dataPointHosp)
                deaths.add(dataPointDeath)
            }
        }

        val positiveDataset = Dataset(positives)
        val hospitalizationDataset = Dataset(hospitalizations)
        val deathDataset = Dataset(deaths)

        val chartStyle = LiveChartStyle().apply {
            mainColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            pathStrokeWidth = 8f
            secondColor = Color.CYAN
            secondPathStrokeWidth = 8f
            textColor = ContextCompat.getColor(requireContext(), R.color.colorText)
        }

        liveChart.setDataset(positiveDataset)
            .setLiveChartStyle(chartStyle)
            .drawBaselineFromFirstPoint()
            .drawFill(true)
            .drawSmoothPath()
            .drawVerticalGuidelines(steps = 4)
            .drawHorizontalGuidelines(steps = 4)
            .setOnTouchCallbackListener(object : LiveChart.OnTouchCallback {
                override fun onTouchCallback(point: DataPoint) {
                    //Display specific data here based on what point the user touches
                    liveChart
                        .parent
                        .requestDisallowInterceptTouchEvent(true)

                    Timber.d(point.x.toString() + " x")
                    Timber.d(point.y.toString() + " y")
                    Timber.d(usList[point.x.toInt()].date.toString())
                    Timber.d(usList[point.x.toInt()].positive.toString())

                    val value = usList[point.x.toInt()]

                    bottom_body_four.text = value.date.toString()
                    bottom_body_three.text = value.positiveIncrease.toString()
                    if (value.death.toString() != "null" || value.death != null) {
                        bottom_body_two.text = value.death.toString()
                    } else {
                        bottom_body_two.text = "0"
                    }
                    bottom_body_one.text =
                        value.positive.toString() + "(" + value.totalTestResults.toString() + ")"


                }

                override fun onTouchFinished() {
                    liveChart.parent.requestDisallowInterceptTouchEvent(true)
                }
            })
            .drawDataset()
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