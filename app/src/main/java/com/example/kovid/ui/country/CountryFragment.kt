package com.example.kovid.ui.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kovid.R
import com.example.kovid.databinding.FragmentCountryBinding
import com.example.kovid.utils.Resource
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
        setupObservers()
        //call fragment setup functions here
    }

    private fun setupObservers(){
        viewModel.USValue.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Timber.d("SUCCESS")
                    Timber.d(it.data?.lastModified)
                    binding.textView.text = it.data?.lastModified
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
}