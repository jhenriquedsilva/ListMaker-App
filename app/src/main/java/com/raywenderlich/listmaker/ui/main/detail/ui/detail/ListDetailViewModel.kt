package com.raywenderlich.listmaker.ui.main.detail.ui.detail

import androidx.lifecycle.ViewModel
import com.raywenderlich.listmaker.models.TaskList

class ListDetailViewModel : ViewModel() {

    lateinit var list: TaskList
    lateinit var onTaskAdded: () -> Unit

    fun addTask(task: String) {
        list.tasks.add(task)
        onTaskAdded()
    }

}