package com.raywenderlich.listmaker.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.listmaker.models.TaskList
import com.raywenderlich.listmaker.databinding.MainFragmentBinding

class MainFragment : Fragment(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    lateinit var clickListener: MainFragmentInteractListener

    interface MainFragmentInteractListener {
        fun listItemTapped(list: TaskList)
    }

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // The bundle that was passed in MainActivity
        // val someInt = requireArguments().getInt("some_int")
        binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.listsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity())))
            .get(MainViewModel::class.java)

        val recyclerViewAdapter = ListSelectionRecyclerViewAdapter(viewModel.lists,this)

        binding.listsRecyclerview.adapter = recyclerViewAdapter

        viewModel.onListAdded = { recyclerViewAdapter.listsUpdated() }
    }

    override fun listItemClicked(list: TaskList) {
        clickListener.listItemTapped(list)
    }

}