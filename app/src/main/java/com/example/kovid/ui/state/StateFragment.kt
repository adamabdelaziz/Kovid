package com.example.kovid.ui.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.kovid.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StateFragment : Fragment() {

    private lateinit var stateViewModel: StateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stateViewModel =
            ViewModelProviders.of(this).get(StateViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_state, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        stateViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}