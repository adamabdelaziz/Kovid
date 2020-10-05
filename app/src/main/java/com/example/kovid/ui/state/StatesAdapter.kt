package com.example.kovid.ui.state

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kovid.data.entities.StateMetadata
import com.example.kovid.databinding.ItemStateBinding


class StatesAdapter(private val fragment: StateListFragment) :
    ListAdapter<StateMetadata, StatesAdapter.ViewHolder>(StateDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class ViewHolder private constructor(
        val binding: ItemStateBinding,
        val fragment: StateListFragment
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StateMetadata) {
            binding.state = item
            binding.fragment = fragment
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemStateBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.findFragment())
            }
        }


    }


}


class StateDiffCallback : DiffUtil.ItemCallback<StateMetadata>() {
    override fun areItemsTheSame(oldItem: StateMetadata, newItem: StateMetadata): Boolean {
        return oldItem.state == oldItem.state
    }

    override fun areContentsTheSame(oldItem: StateMetadata, newItem: StateMetadata): Boolean {
        return oldItem == newItem
    }


}


//    private val states = ArrayList<StateMetadata>()
//
//    fun setItems(items: ArrayList<StateMetadata>) {
//        this.states.clear()
//        this.states.addAll(items)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
//        val binding: ItemStateBinding =
//            ItemStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return StateViewHolder(binding, listener)
//    }
//
//    override fun onBindViewHolder(holder: StateViewHolder, position: Int) = holder.bind(states[position])
//
//    override fun getItemCount(): Int = states.size
//}
//
//class StateViewHolder(
//    private val itemBinding: ItemStateBinding,
//    private val listener: StatesAdapter.StateListener
//) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {
//
//    private lateinit var stateMetadata: StateMetadata
//
//    override fun onClick(p0: View?) {
//        listener.onClickedState(stateMetadata.state)
//    }
//
//    @SuppressLint("SetTextI18n")
//    fun bind(item: StateMetadata) {
//        this.stateMetadata = item
//        itemBinding.stateName.text = item.name
//        Timber.d("%sBinded", item.name)
//    }
//
//