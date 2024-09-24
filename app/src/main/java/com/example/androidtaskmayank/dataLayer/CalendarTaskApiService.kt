package com.example.androidtaskmayank.dataLayer

import com.example.androidtaskmayank.models.TaskStatusResponse
import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.TaskListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface CalendarTaskApiService {

    @POST("api/storeCalendarTask")
    suspend fun storeCalendarTask(@Body storeData: StoreDataRequest): Response<TaskStatusResponse>

    @POST("api/getCalendarTaskList")
    suspend fun fetchAllCalenderTasks(@Body getData: Map<String, Int>): Response<TaskListResponse>

    @POST("api/deleteCalendarTask")
    suspend fun deleteCalendarTask(@Body deleteTask: Map<String, Int>): Response<TaskStatusResponse>

}

