package com.example.kovid.ui.state

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kovid.MainActivity
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
            viewModel.getState(it)
            stateID = it
            Timber.d(it + "stateFragment onViewCreated called")
        }
        setupObservers()
        setupHistoricData(stateID)
        //setup functions
    }

    private fun setupHistoricData(state: String) {
        var historic = viewModel.getHistoricData(state)

        historic.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("historic data success")

                    val list = it.data

                    if (list != null) {
                        Timber.d("historic list not null")
                        Timber.d(list.size.toString())
                        if (isAdded) {
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

    private fun setupObservers() {
        viewModel.states.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("sssss metadata SUCCESS")
                    binding.progressBar.visibility = View.GONE
                    //binding.gridLayout.visibility = View.VISIBLE

                    //using the 2 letter state ID passed in from StateListFragment,
                    // get the actual data you need pertaining to the state and bind it in bindData()

                    val list = it.data
                    if (list != null) {
                        Timber.d("list not null")
                        for (i in list) {
                            if (i.state == stateID) {
                                Timber.d("matching state%s", i.state)
                                bindData(i)

                            }
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    Timber.d("stateFragment metadata  ERROR")

                }
                Resource.Status.LOADING -> {
                    Timber.d("stateFragment metadata LOADING")
                    binding.progressBar.visibility = View.VISIBLE
                   // binding.gridLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun setupChart(stateValueList: List<StateValue>) {
        val liveChart = binding.liveChart
        val positives = mutableListOf<DataPoint>()
        val negatives = mutableListOf<DataPoint>()
        positives.clear()
        negatives.clear()
        Timber.d("setup chart called")

        // need to go through the list from the back
        val stateList = stateValueList.reversed()

        for ((index, value) in stateList.withIndex()) {

            if(value.positive != null && value.negative != null)
            {
                val dataPointPos = DataPoint(index.toFloat(), value.positive.toFloat())
                val dataPointNeg = DataPoint(index.toFloat(), value.negative.toFloat())
                positives.add(dataPointPos)
                negatives.add(dataPointNeg)
            }

            Timber.d(index.toString() + " index")
            Timber.d(value.positive.toString() + " positive")
            Timber.d(value.negative.toString() + " negative")
            Timber.d(positives.size.toString() + " positives list size")
            Timber.d(negatives.size.toString() + " negatives list size")

        }

        val positiveDataset = Dataset(positives)
        val negativeDataset = Dataset(negatives)

        val chartStyle = LiveChartStyle().apply {
            mainColor = Color.RED
            pathStrokeWidth = 8f
            secondColor = Color.CYAN
            secondPathStrokeWidth = 8f
            textColor=ContextCompat.getColor(requireContext(),R.color.colorText)
        }

        liveChart.setDataset(positiveDataset)
            .setLiveChartStyle(chartStyle)
            .drawBaseline()
            .drawYBounds()
            .drawFill(true)
            //.setSecondDataset(negativeDataset)
            .drawSmoothPath()
            .drawVerticalGuidelines(steps=4)
            .drawHorizontalGuidelines(steps=4)
            .setOnTouchCallbackListener(object : LiveChart.OnTouchCallback{
                override fun onTouchCallback(point: DataPoint) {

                }

                override fun onTouchFinished() {

                }
            })
            .drawDataset()
    }

    private fun bindData(stateValue: StateValue) {
        Timber.d("bindData called")
        Timber.d(stateValue.hash)
        Timber.d(stateValue.state)



    }
}