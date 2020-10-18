package com.example.kovid.ui.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kovid.data.entities.StateValue
import com.example.kovid.databinding.FragmentStateBinding
import com.example.kovid.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
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

                    var list = it.data
                    if (list != null) {
                        Timber.d("historic list not null")
                        for (i in list) {
                            Timber.d(i.date.toString())
                            Timber.d(list.size.toString())
                            Timber.d(i.state)
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
                    binding.stateConstraintLayout.visibility = View.VISIBLE

                    //using the 2 letter state ID passed in from StateListFragment, get the actual data you need pertaining to the state and bind it in bindData()
                    var list = it.data
                    if (list != null) {
                        Timber.d("list not null")
                        for (i in list) {
                            if (i.state == stateID) {
                                Timber.d("matching state " + i.state)
                                bindData(i)

                            }
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    Timber.d("ssss metadata  ERROR")

                }
                Resource.Status.LOADING -> {
                    Timber.d("ssss metadata LOADING")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.stateConstraintLayout.visibility = View.GONE
                }
            }
        })
    }

    private fun setupChart(stateValue: StateValue) {
//        val liveChart = binding.liveChart
//
//        val dataset = Dataset(
//            mutableListOf(
//                DataPoint(0f, 1f), DataPoint(1f, 3f),
//                DataPoint(2f, 6f)
//            )
//        )
//
//        liveChart.setDataset(dataset)
//            .drawYBounds()
//            .drawBaseline()
//            .drawFill()
//            .drawDataset()
    }

    private fun bindData(stateValue: StateValue) {
        Timber.d("bindData called")
        Timber.d(stateValue.hash)
        Timber.d(stateValue.state)


    }
}