package com.example.androidtaskmayank.dataLayer

import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.Task
import com.example.androidtaskmayank.models.TaskDetail
import com.example.androidtaskmayank.utils.testingCalendarTasksJson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CalendarTaskApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var calendarTaskApi: CalendarTaskApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        calendarTaskApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CalendarTaskApiService::class.java)

    }

    @Test
    fun handleSuccess_GetAllCalendarTasks() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(testingCalendarTasksJson)
        }

        mockWebServer.enqueue(mockResponse)

        val response = calendarTaskApi.fetchAllCalenderTasks(mapOf("user_id" to 1))

        mockWebServer.takeRequest()

        Assert.assertEquals("Is Body empty", false, response.body()?.tasks.isNullOrEmpty())
        Assert.assertEquals("Check Response ", true, response.isSuccessful)
        Assert.assertEquals("Response Code", 200, response.code())

        Assert.assertEquals("Check Body Size ", 4, response.body()?.tasks?.size)
        Assert.assertEquals(
            "Check Title for 1st Task",
            "Party",
            response.body()?.tasks?.get(0)?.taskDetail?.title
        )
    }

    @Test
    fun handleFailure_GetAllCalendarTasks() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(500)
            setBody("Internal Server Error")
        }
        mockWebServer.enqueue(mockResponse)

        val response = calendarTaskApi.fetchAllCalenderTasks(mapOf("user_id" to 1))
        mockWebServer.takeRequest()

        Assert.assertEquals("Check Response ", false, response.isSuccessful)
        Assert.assertEquals("Response Code", 500, response.code())

        Assert.assertEquals("Is Body empty", true, response.body()?.tasks.isNullOrEmpty())
        Assert.assertEquals(
            "Error Message",
            "Internal Server Error",
            response.errorBody()?.string()
        )
    }

    @Test
    fun handleSuccess_StoreCalendarTask() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(
                "{\n" +
                        "      \"status\": \"Successful\"\n" +
                        "    }"
            )
        }
        mockWebServer.enqueue(mockResponse)

        val storeDataRequest =
            StoreDataRequest(1, TaskDetail(123456789, "Title", "Description"))
        val response = calendarTaskApi.storeCalendarTask(storeDataRequest)
        mockWebServer.takeRequest()

        Assert.assertEquals("Check Response ", true, response.isSuccessful)
        Assert.assertEquals("Response Code", 200, response.code())
        Assert.assertEquals("Success Message", "Successful", response.body()?.status)
    }

    @Test
    fun handleFailure_CalendarTask() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(401)
            setBody("Unauthorized")
        }
        mockWebServer.enqueue(mockResponse)

        val storeDataRequest =
            StoreDataRequest(1, TaskDetail(123456789, "Title", "Description"))
        val response = calendarTaskApi.storeCalendarTask(storeDataRequest)
        mockWebServer.takeRequest()

        Assert.assertEquals("Check Response ", false, response.isSuccessful)
        Assert.assertEquals("Response Code", 401, response.code())
        Assert.assertEquals("Error Message", "Unauthorized", response.errorBody()?.string())

    }

    @Test
    fun handleSuccess_DeleteCalendarTask() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(200)
            setBody(
                "{\n" +
                        "      \"status\": \"Successful\"\n" +
                        "    }"
            )
        }
        mockWebServer.enqueue(mockResponse)

        val response = calendarTaskApi.deleteCalendarTask(mapOf("user_id" to 0, "task_id" to 1))
        mockWebServer.takeRequest()

        Assert.assertEquals("Check Response ", true, response.isSuccessful)
        Assert.assertEquals("Response Code", 200, response.code())
        Assert.assertEquals("Success Message", "Successful", response.body()?.status)
    }

    @Test
    fun handleFailure_DeleteCalendarTask() = runTest {
        val mockResponse = MockResponse().apply {
            setResponseCode(404)
            setBody("Not Found")
        }
        mockWebServer.enqueue(mockResponse)

        val response = calendarTaskApi.deleteCalendarTask(mapOf("user_id" to 0, "task_id" to 1))
        mockWebServer.takeRequest()


        Assert.assertEquals("Check Response ", false, response.isSuccessful)
        Assert.assertEquals("Response Code", 404, response.code())
        Assert.assertEquals("Error Message", "Not Found", response.errorBody()?.string())

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


}

