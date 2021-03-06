package com.raywenderlich.listmaker.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.raywenderlich.listmaker.models.TaskList

class MainViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    // Inform other interested classes when a list is added to the app
    lateinit var onListAdded: (() -> Unit)
    lateinit var onTaskAdded: () -> Unit
    lateinit var list: TaskList

    // It will be initialized when requested only
    // It will be populated with the return of retrieveLists()
    val lists: MutableList<TaskList> by lazy { retrieveLists() }

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded()
    }

    // Gets data
    private fun retrieveLists(): MutableList<TaskList> {
        val sharedPreferencesContents = sharedPreferences.all // Returns Map<String, ?>
        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferencesContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)
            taskLists.add(list)
        }

        return taskLists
    }

    // Saves data
    fun saveList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)
        onListAdded()
    }

    fun updateList(list: TaskList) {
        sharedPreferences.edit().putStringSet(list.name, list.tasks.toHashSet()).apply()
        lists.add(list)
    }

    fun refreshLists() {
        lists.clear()
        lists.addAll(retrieveLists())
    }
}