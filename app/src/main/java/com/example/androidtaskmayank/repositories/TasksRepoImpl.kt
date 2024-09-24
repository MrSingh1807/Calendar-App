package com.example.androidtaskmayank.repositories

import com.example.androidtaskmayank.dataLayer.CalendarTaskApiService
import com.example.androidtaskmayank.models.TaskStatusResponse
import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.TaskListResponse
import com.example.androidtaskmayank.utils.NetworkResult
import com.example.androidtaskmayank.utils.USER_ID
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject


@ViewModelScoped
class TasksRepoImpl @Inject constructor(private val accessApi: CalendarTaskApiService) : TasksRepo {

    override suspend fun storeCalendarTask(storeData: StoreDataRequest): NetworkResult<TaskStatusResponse?> {
        val response = accessApi.storeCalendarTask(storeData)
        return when (response.code()) {
            200 -> NetworkResult.Success(response.body())
            else -> NetworkResult.Error(response.errorBody()?.string())
        }
    }

    override suspend fun fetchCalenderTasks(): NetworkResult<TaskListResponse?> {
        val response = accessApi.fetchAllCalenderTasks(mapOf("user_id" to USER_ID))
        return when (response.code()) {
            200 -> NetworkResult.Success(response.body())
            else -> NetworkResult.Error(response.errorBody()?.string())
        }
    }

    override suspend fun deleteTask(taskId: Int): NetworkResult<TaskStatusResponse?> {
        val response = accessApi.deleteCalendarTask(mapOf("user_id" to USER_ID, "task_id" to taskId))
        return when (response.code()) {
            200 -> NetworkResult.Success(response.body())
            else -> NetworkResult.Error(response.errorBody()?.string())
        }
    }

}
