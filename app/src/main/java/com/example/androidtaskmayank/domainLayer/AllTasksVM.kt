package com.example.androidtaskmayank.domainLayer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtaskmayank.models.Task
import com.example.androidtaskmayank.repositories.TasksRepo
import com.example.androidtaskmayank.utils.NetworkConnectivity
import com.example.androidtaskmayank.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AllTasksVM @Inject constructor(
    private val connectivity: NetworkConnectivity,
    private val tasksRepo: TasksRepo
) : ViewModel() {

    private val mutableTaskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList = mutableTaskList.asStateFlow()

    private val mutableToastMessage = MutableStateFlow<String?>(null)
    val toastMessage = mutableToastMessage.filterNotNull()

    init {
        Log.i("MrSingh", " Network Availability : ${connectivity.isNetworkConnected()} ")

        getAllTasks()
    }

    private fun getAllTasks() {
        if (connectivity.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = tasksRepo.fetchCalenderTasks()) {
                    is NetworkResult.Error -> mutableToastMessage.emit(response.message)
                    is NetworkResult.Success -> {
                        response.data?.let {
                            Log.i("MrSingh", " Task Models:  $it ")
                            mutableTaskList.emit(it.tasks.sortedBy { it.taskDetail.date })
                        }
                    }
                }
            }
        }
    }

    fun deleteTask(taskId: Int) {
        if (connectivity.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = tasksRepo.deleteTask(taskId)) {
                    is NetworkResult.Error -> mutableToastMessage.emit(response.message)
                    is NetworkResult.Success -> {
                        mutableToastMessage.emit("Task Deleted Successfully")
                        mutableTaskList.emit(mutableTaskList.value.filter { it.taskId != taskId })
                    }
                }
            }
        }
    }

}
