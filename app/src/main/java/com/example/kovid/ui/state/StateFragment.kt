package com.example.kovid.ui.state

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kovid.R
import com.example.kovid.data.entities.StateValue
import com.example.kovid.databinding.FragmentStateBinding
import com.example.kovid.utils.Resource
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChart
import com.yabu.livechart.view.LiveChartStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_state.*
import timber.log.Timber

@AndroidEntryPoint
class StateFragment : Fragment() {

    private var _binding: FragmentStateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StateViewModel by viewModels()
    private lateinit var stateID: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("id")?.let {
            //viewModel.getState(it)
            stateID = it
            Timber.d(it + "stateFragment onViewCreated called")
        }
        //setup functions
        // setupObservers()
        setupObservers(stateID)
        setupHistoricData(stateID)

    }

    private fun setupHistoricData(state: String) {
        val historic = viewModel.getHistoricData(state)
        //Get historic data for a specific date from the viewModel
        historic.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("historic data success")

                    val list = it.data
                    //I dont think isAdded is necessary here but this plots the data for the chart
                    // by calling setupChart()
                    if (list != null) {
                        Timber.d("historic list not null")
                        Timber.d(list.size.toString() + " historic list size")
                        if (list.size > 10) {
                            //this is because for whatever fucking reason the list size will be 1
                            //and then a few seconds later it will be the correct full length size
                            setupChart(list)
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Timber.d("historic data error")
                }
                Resource.Status.LOADING -> {
                    Timber.d("historic data loading")
                }
            }
        })
    }

    private fun setupObservers(state: String) {
        val current = viewModel.getCurrentData(state)
        current.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    //binding.progressBar.visibility = View.GONE
                    binding.constraintLayout.visibility = View.VISIBLE

                    Timber.d("observers success")
                    val data = it.data

                    if (data != null) {
                        Timber.d("binding data")
                        bindData(data)
                    }

                }
                Resource.Status.ERROR -> {
                    Timber.d("observer  error")
                }
                Resource.Status.LOADING -> {
                    Timber.d("observer loading")
                    //binding.progressBar.visibility = View.VISIBLE
                    binding.constraintLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun setupChart(stateValueList: List<StateValue>) {
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

        //reversed() puts it in the correct order chronologically
        //drop(n-90) gives the past 90 days only
        val stateList = stateValueList.reversed().drop(stateValueList.size-89)

        for ((index, value) in stateList.withIndex()) {

            if (value.positive != null && value.totalTestResults != null && value.hospitalizedCurrently != null && value.death != null) {
                val dataPointPos = DataPoint(index.toFloat(), value.positive.toFloat())
                val dataPointHosp =
                    DataPoint(index.toFloat(), value.hospitalizedCurrently.toFloat())
                val dataPointDeath = DataPoint(index.toFloat(), value.death.toFloat())
                Timber.d(index.toString() + " TRUE INDEX??")

                positives.add(dataPointPos)
                hospitializations.add(dataPointHosp)
                deaths.add(dataPointDeath)


            }
        }

        val positiveDataset = Dataset(positives)
        val hospitalizationDataset = Dataset(hospitializations)
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
                    Timber.d(stateList[point.x.toInt()].date.toString())
                    Timber.d(stateList[point.x.toInt()].positive.toString())

                    val value = stateList[point.x.toInt()]

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

        liveChartTwo.setDataset(hospitalizationDataset)
            .setLiveChartStyle(chartStyle)
            .drawBaseline()
            .drawFill(true)
            .drawSmoothPath()
            .drawVerticalGuidelines(steps = 4)
            .drawHorizontalGuidelines(steps = 4)
            .setOnTouchCallbackListener(object : LiveChart.OnTouchCallback {
                override fun onTouchCallback(point: DataPoint) {
                    liveChart.parent.requestDisallowInterceptTouchEvent(true)

                    val value = stateList[point.x.toInt()]
                    if (value.hospitalizedCurrently.toString() != "null" || value.hospitalizedCurrently != null) {
                        lower_body_one.text = value.hospitalizedCurrently.toString()
                    } else {
                        lower_body_one.text = "0"
                    }
                    lower_body_two.text = value.date.toString()
                }

                override fun onTouchFinished() {
                    liveChart.parent.requestDisallowInterceptTouchEvent(true)
                }
            })
            .drawDataset()

        liveChartThree.setDataset(deathDataset)
            .setLiveChartStyle(chartStyle)
            .drawBaseline()
            .drawFill(true)
            .drawSmoothPath()
            .drawVerticalGuidelines(steps = 4)
            .drawHorizontalGuidelines(steps = 4)
            .setOnTouchCallbackListener(object : LiveChart.OnTouchCallback {
                override fun onTouchCallback(point: DataPoint) {
                    liveChart.parent.requestDisallowInterceptTouchEvent(true)
                    val value = stateList[point.x.toInt()]

                    if (value.death.toString() != "null" || value.death != null) {
                        lowest_body_one.text = value.death.toString()
                    } else {
                        lowest_body_one.text = "0"
                    }
                    lowest_body_two.text = value.date.toString()
                }

                override fun onTouchFinished() {
                    liveChart.parent.requestDisallowInterceptTouchEvent(true)
                }
            })
            .drawDataset()
    }


    private fun bindData(stateValue: StateValue) {
        Timber.d("bindData called")
        binding.stateValue = stateValue
    }
}

