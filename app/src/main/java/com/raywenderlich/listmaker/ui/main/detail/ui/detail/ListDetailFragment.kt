package com.raywenderlich.listmaker.ui.main.detail.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.listmaker.databinding.ListDetailFragmentBinding

class ListDetailFragment : Fragment() {

    lateinit var binding: ListDetailFragmentBinding

    companion object {
        fun newInstance() = ListDetailFragment()
    }

    private lateinit var viewModel: ListDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListDetailFragmentBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ListDetailViewModel::class.java)

        val recyclerAdapter = ListItemsRecyclerViewAdapter(viewModel.list)
        binding.listItemRecyclerview.adapter = recyclerAdapter
        binding.listItemRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        // Inside the lambda, the adapter is notified that the list of tasks has updated. This
        // causes the RecyclerView to be redrawn, showing any new items
        viewModel.onTaskAdded = { recyclerAdapter.notifyDataSetChanged() }
    }

}