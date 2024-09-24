package com.example.androidtaskmayank.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidtaskmayank.dataLayer.CalendarTaskApiService
import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.Task
import com.example.androidtaskmayank.models.TaskDetail
import com.example.androidtaskmayank.models.TaskListResponse
import com.example.androidtaskmayank.models.TaskStatusResponse
import com.example.androidtaskmayank.utils.NetworkResult
import com.example.androidtaskmayank.utils.USER_ID
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response


class TasksRepoImplTest {

    @Mock
    lateinit var tasksRepo: CalendarTaskApiService

    private lateinit var taskRepoImpl: TasksRepoImpl

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        taskRepoImpl = TasksRepoImpl(tasksRepo)
    }

    @Test
    fun testSuccess_fetchCalenderTasks() = runTest {
        val demoResponse = TaskListResponse(
            listOf(
                Task(TaskDetail(1, "walking in park", "Go"), 1),
                Task(TaskDetail(2, "Take rest", "Sleep"), 2),
                Task(TaskDetail(3, "Do some work", "Workout"), 3)
            )
        )
        Mockito.`when`(tasksRepo.fetchAllCalenderTasks(mapOf("user_id" to USER_ID)))
            .thenReturn(Response.success(200, demoResponse))

        val response = taskRepoImpl.fetchCalenderTasks()

        assertEquals("Response Status =>  ", true, response is NetworkResult.Success)
        assertEquals("Response Data =>  ", demoResponse, response.data)
        assertEquals(
            "Response Second Item Title: =>  ",
            "Sleep",
            response.data!!.tasks[1].taskDetail.title
        )
    }

    @Test
    fun testFailure_FetchCalenderTasks() = runTest {
        Mockito.`when`(tasksRepo.fetchAllCalenderTasks(mapOf("user_id" to USER_ID)))
            .thenReturn(Response.error(401, "Unauthorized".toResponseBody()))

        val response = taskRepoImpl.fetchCalenderTasks()

        assertEquals("Response Status =>  ", true, response is NetworkResult.Error)
        assertEquals("Response Message =>  ", "Unauthorized", response.message)
    }

    @Test
    fun testSuccess_storeCalendarTask() = runTest {
        val storeDataRequest =
            StoreDataRequest(1, TaskDetail(123456789, "Title", "Description"))
        Mockito.`when`(tasksRepo.storeCalendarTask(storeDataRequest))
            .thenReturn(Response.success(TaskStatusResponse("Successful")))

        val response = taskRepoImpl.storeCalendarTask(storeDataRequest)

        assertEquals("Response Status =>  ", true, response is NetworkResult.Success)
        assertEquals("Response Status Message =>  ", "Successful", response.data?.status)
    }

    @Test
    fun testFailure_storeCalendarTask() = runTest {
        val storeDataRequest =
            StoreDataRequest(1, TaskDetail(123456789, "Title", "Description"))
        Mockito.`when`(tasksRepo.storeCalendarTask(storeDataRequest))
            .thenReturn(Response.error(400, "Bad Request".toResponseBody()))

        val response = taskRepoImpl.storeCalendarTask(storeDataRequest)

        assertEquals("Response Status =>  ", true, (response is NetworkResult.Error))
        assertEquals("Response Message =>  ", "Bad Request", response.message)
    }

    @Test
    fun testSuccess_deleteTask() = runTest {
        Mockito.`when`(tasksRepo.deleteCalendarTask(mapOf("user_id" to USER_ID, "task_id" to 2)))
            .thenReturn(Response.success(200, TaskStatusResponse("Successful")))

        val response = taskRepoImpl.deleteTask(2)

        assertEquals("Response Status =>  ", true, response is NetworkResult.Success)
        assertEquals("Response Message =>  ", "Successful", response.data?.status)
    }

    @Test
    fun testFailure_deleteTask() = runTest {
        Mockito.`when`(tasksRepo.deleteCalendarTask(mapOf("user_id" to USER_ID, "task_id" to 2)))
            .thenReturn(Response.error(401, "Unauthorized".toResponseBody()))

        val response = taskRepoImpl.deleteTask(2)

        assertEquals("Response Status =>  ", true, response is NetworkResult.Error)
        assertEquals("Response Message =>  ", "Unauthorized", response.message)
    }

}