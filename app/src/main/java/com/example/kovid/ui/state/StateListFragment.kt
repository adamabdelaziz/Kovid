package com.example.kovid.ui.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kovid.data.entities.StateMetadata
import com.example.kovid.databinding.FragmentStateListBinding
import com.example.kovid.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StateListFragment : Fragment() {
    //StatesAdapter.StateListener
    private var _binding: FragmentStateListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StateViewModel by viewModels()
    private lateinit var adapter: StatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStateListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //call setup functions here
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = StatesAdapter(this)
        binding.stateList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.stateList.adapter = adapter
    }

    private fun setupObservers() {


        viewModel.stateMetadata.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("State metadata SUCCESS")
                    if (!it.data.isNullOrEmpty()) {
                        //remove weird state entries from API
                        filterStates(it.data)
                    }
                }
                Resource.Status.ERROR -> {
                    Timber.d("State metadata  ERROR")

                }
                Resource.Status.LOADING -> {
                    Timber.d("State metadata LOADING")
                }
            }
        })
    }

    private fun filterStates(data: List<StateMetadata>) {

        val badStates = setOf("GU", "AS", "VI", "PR", "MP")
        val cleanedList: MutableList<StateMetadata> = mutableListOf()

        for (i in data) {
            if (i.state !in badStates) {
                cleanedList.add(i)
                Timber.d(i.state + " added to doom list ")
            }
        }

        adapter.submitList(cleanedList)

    }

    fun onClickedState(stateId: String) {
        Timber.d(" %sclicked ", stateId)
        //Go to state details screen of specific state
    }


}


//    private fun setupRecyclerView() {
//        adapter = StatesAdapter(this)
//        binding.stateList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
//        binding.stateList.setHasFixedSize(true)
//        binding.stateList.adapter = adapter
//    }
//

//
//    override fun onClickedState(stateId: String) {
//        //parcelable navigation to StateFragment based on the state the user clicked
//        Timber.d(stateId + "clicked")
//    }