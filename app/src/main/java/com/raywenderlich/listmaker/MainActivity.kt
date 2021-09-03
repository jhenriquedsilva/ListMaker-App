package com.raywenderlich.listmaker

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.raywenderlich.listmaker.databinding.MainActivityBinding
import com.raywenderlich.listmaker.models.TaskList
import com.raywenderlich.listmaker.ui.main.detail.ListDetailActivity
import com.raywenderlich.listmaker.ui.main.MainFragment
import com.raywenderlich.listmaker.ui.main.MainViewModel
import com.raywenderlich.listmaker.ui.main.MainViewModelFactory
import com.raywenderlich.listmaker.ui.main.detail.ui.detail.ListDetailFragment

class MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractListener {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                                                                                        // This is the default shared preference of my app used to store app settings
        viewModel = ViewModelProvider(this, MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))).get(MainViewModel::class.java)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            /*
            If my activity needs some initial state, I can pass a bundle to it via the add method
            Another way to create an activity
            val bundle = bundleOf("some_int" to 0)
            supportFragmentManager.commit { setReorderingAllowed(true); add<MainFragment>(R.id.fragment_countainer_view, args = bundle) }
            supportFragmentManager.commit {
                replace(R.id.container, MainFragment.newInstance())
                setReorderingAllowed(true)
                addToBackStack(null)
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commitNow()

            val mainFragment = MainFragment.newInstance(this)
            supportFragmentManager.beginTransaction().replace(R.id.detail_container, mainFragment).commitNow()
            */
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this

            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.detail_container // The smaller layout is used
            } else {
                R.id.main_fragment_container
            }

            // Fragment manager creates a fragment transaction
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment) // Instantiate the fragment
            }

        }

        binding.fabButton.setOnClickListener { showCreateListDialog() }
    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT

        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()

            val taskList = TaskList(listTitleEditText.text.toString())
            viewModel.saveList(taskList)
            showListDetail(taskList)
        }

        builder.create().show()
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) {dialog, _ ->
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LIST_DETAIL_REST_CODE && resultCode == Activity.RESULT_OK) {
            // Make sure that there is a content
            data?.let { data ->
                viewModel.updateList(data.getParcelableExtra(INTENT_LIST_KEY)!!)
                viewModel.refreshLists()
            }
        }
    }

    // How to create an Intent and share data with another Activity
    private fun showListDetail(list: TaskList) {
        if (binding.mainFragmentContainer == null) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivityForResult(listDetailIntent, LIST_DETAIL_REST_CODE)
        } else {
            val bundle = bundleOf(INTENT_LIST_KEY to list)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.list_detail_fragment_container,ListDetailFragment::class.java, bundle, null)
            }
            binding.fabButton.setOnClickListener {
                showCreateTaskDialog()
            }
        }

    }

    override fun onBackPressed() {

        val listDetailFragment =
            supportFragmentManager.findFragmentById(R.id.list_detail_fragment_container)

        if (listDetailFragment == null) {
            super.onBackPressed()
        } else {
            title = resources.getString(R.string.app_name)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(listDetailFragment)
            }

            binding.fabButton.setOnClickListener {
                showCreateListDialog()
            }
        }
    }

    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REST_CODE = 123
    }

    override fun listItemTapped(list: TaskList) {
        showListDetail(list)
    }
}