package com.example.androidtaskmayank.repositories

import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.TaskListResponse
import com.example.androidtaskmayank.models.TaskStatusResponse
import com.example.androidtaskmayank.utils.NetworkResult


interface TasksRepo {

    suspend fun storeCalendarTask(storeDataRequest: StoreDataRequest): NetworkResult<TaskStatusResponse?>

    suspend fun fetchCalenderTasks(): NetworkResult<TaskListResponse?>

    suspend fun deleteTask(taskId: Int): NetworkResult<TaskStatusResponse?>

}
