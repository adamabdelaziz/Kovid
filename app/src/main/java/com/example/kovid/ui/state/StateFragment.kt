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
                        Timber.d(list.size.toString())
                        if (list.size > 2) {
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
                    binding.progressBar.visibility = View.GONE
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
                    binding.progressBar.visibility = View.VISIBLE
                    binding.constraintLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun setupChart(stateValueList: List<StateValue>) {
        val liveChart = binding.liveChart
        val positives = mutableListOf<DataPoint>()
        val negatives = mutableListOf<DataPoint>()
        // val totalTests = mutableListOf<DataPoint>()
        positives.clear()
        negatives.clear()
        Timber.d("setup chart called")

        // need to go through the list from the back
        val stateList = stateValueList.reversed()

        for ((index, value) in stateList.withIndex()) {
            if (value.positive != null && value.negative != null && value.totalTestResults != null) {
                val dataPointPos = DataPoint(index.toFloat(), value.positive.toFloat())
                val dataPointNeg = DataPoint(index.toFloat(), value.negative.toFloat())
                //val dataPointTests = DataPoint(index.toFloat(), value.totalTestResults.toFloat())
                positives.add(dataPointPos)
                negatives.add(dataPointNeg)
                //totalTests.add(dataPointTests)
            }

            Timber.d(index.toString() + " index")
            Timber.d(value.positive.toString() + " positive")
            Timber.d(value.negative.toString() + " negative")
            Timber.d(positives.size.toString() + " positives list size")
            Timber.d(negatives.size.toString() + " negatives list size")

        }

        val positiveDataset = Dataset(positives)
        //val negativeDataset = Dataset(negatives)
        //val testsDataset = Dataset(totalTests)

        val chartStyle = LiveChartStyle().apply {
            mainColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            pathStrokeWidth = 8f
            secondColor = Color.CYAN
            secondPathStrokeWidth = 8f
            textColor = ContextCompat.getColor(requireContext(), R.color.colorText)
        }

        liveChart.setDataset(positiveDataset)
            .setLiveChartStyle(chartStyle)
            .drawBaseline()
            //.drawYBounds()
            .drawFill(true)
            //.setSecondDataset(testsDataset)
            .drawSmoothPath()
            .drawVerticalGuidelines(steps = 4)
            .drawHorizontalGuidelines(steps = 4)
            .setOnTouchCallbackListener(object : LiveChart.OnTouchCallback {
                override fun onTouchCallback(point: DataPoint) {
                    //Display specific data here based on what point the user touches

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
                    bottom_body_one.text = value.positive.toString() +"("+ value.totalTestResults.toString() + ")"


                }

                override fun onTouchFinished() {
//                    bottom_card_four_body.text = ""
//                    bottom_card_three_body.text =""
//                    bottom_card_two_body.text = ""
//                    bottom_card_one_body.text =""
                }
            })
            .drawDataset()
    }

    private fun bindData(stateValue: StateValue) {

        Timber.d("bindData called")
        binding.stateValue = stateValue

//        val oldDate = stateValue.date.toString()
//        val year = oldDate.subSequence(0, 4).toString()
//        val month = oldDate.subSequence(4,6).toString()
//        val day = oldDate.subSequence(6,8).toString()
//
//        Timber.d(year+ " year")
//        Timber.d(month+ " month")
//        Timber.d(day + " day")
//
//        stateValue.newDate = year + "/" + month + "/" + day

    }
}


//    private fun setupObservers() {
//        viewModel.states.observe(viewLifecycleOwner, Observer {
//            when (it.status) {
//                Resource.Status.SUCCESS -> {
//                    Timber.d("sssss metadata SUCCESS")
//                    binding.progressBar.visibility = View.GONE
//                    binding.linearLayout.visibility = View.VISIBLE
//
//                    //using the 2 letter state ID passed in from StateListFragment,
//                    // get the actual data you need pertaining to the state and bind it in bindData()
//
//                    val list = it.data
//
//                    if (list != null) {
//                        Timber.d("list size is " + list.size.toString())
//                        //Not sure why the 0 value isnt the highest date but this ensures it pulls
//                        // the most recent data for the current data section
//                        var highestDate = it.data[0]
//                        Timber.d("list not null")
//                        for (i in list) {
//                            if (i.state == stateID) {
//                                Timber.d("matching state%s", i.state)
//                                Timber.d("date is " + i.date.toString())
//                                if (i.date > highestDate.date) {
//                                    highestDate = i
//                                }
//                            }
//                        }
//                        bindData(highestDate)
//                    }
//                }
//
//                Resource.Status.ERROR -> {
//                    Timber.d("stateFragment metadata  ERROR")
//
//                }
//                Resource.Status.LOADING -> {
//                    Timber.d("stateFragment metadata LOADING")
//                    binding.progressBar.visibility = View.VISIBLE
//                    binding.linearLayout.visibility = View.GONE
//                }
//            }
//        })
//    }

