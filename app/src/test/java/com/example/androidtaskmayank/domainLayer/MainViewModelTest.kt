package com.example.androidtaskmayank.domainLayer


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.Task
import com.example.androidtaskmayank.models.TaskDetail
import com.example.androidtaskmayank.models.TaskListResponse
import com.example.androidtaskmayank.models.TaskStatusResponse
import com.example.androidtaskmayank.repositories.TasksRepo
import com.example.androidtaskmayank.utils.NetworkConnectivity
import com.example.androidtaskmayank.utils.NetworkResult
import com.example.androidtaskmayank.utils.USER_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Mock
    lateinit var repo: TasksRepo

    @Mock
    lateinit var connectivity: NetworkConnectivity

    lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        vm = MainViewModel(connectivity, repo)
    }

    @Test
    fun isSelectedDateCurrentDateFutureDate() {
        //todo: date : 01-01-2024 - 12:00:00  => 1704090600000
        //todo: date : 01-12-2024 - 12:00:00  => 1733034600000

        assertEquals("Is Future Date", false, vm.isSelectedDateCurrentDateFutureDate(1704090600000))
        assertEquals("Is Future Date ", true, vm.isSelectedDateCurrentDateFutureDate(1733034600000))
    }

    @Test
    fun successTestCase_GetAllDayTask() = runTest {

        Mockito.`when`(repo.fetchCalenderTasks())
            .thenReturn(NetworkResult.Success(TaskListResponse(emptyList())))

        vm.fetchAllTasks()
        val tasks = vm.allTasks
        assertEquals("Task List Size", 0, tasks.size)
    }

    @Test
    fun failedTestCase_GetAllDayTask() = runTest {

        Mockito.`when`(repo.fetchCalenderTasks())
            .thenReturn(NetworkResult.Error("Server Not Responding"))

        vm.fetchAllTasks()
        val tasks = vm.allTasks

        assertEquals("Task List Size", 0, tasks.size)
    }

    private val tasks = mutableListOf<Task>().apply {
        //todo: date : 26-07-2024 - 12:00:00  => 1722004200000
        //todo: date : 26-07-2024 - 20:00:00  => 1721975400000
        //todo: date : 01-7-2024 - 12:00:00  => 1719815400000
        //todo: date : 01-08-2024 - 12:00:00  => 1722522600000
        //todo: date : 01-08-2024 - 20:00:00  => 1722522600000
        add(Task(TaskDetail(1722004200000, "Pizza Party", "Lunch"), 120))
        add(Task(TaskDetail(1721975400000, "Evening Party", "Dinner"), 121))
        add(Task(TaskDetail(1719815400000, "Pizza Party", "Lunch"), 123))
        add(Task(TaskDetail(1722522600000, "Pizza Party", "Lunch"), 124))
        add(Task(TaskDetail(1722522600000, "Evening Party", "Dinner"), 125))
    }

    @Test
    fun test_getSelectedDayTask() = runTest {

        Mockito.`when`(connectivity.isNetworkConnected())
            .thenReturn(true)

        Mockito.`when`(repo.fetchCalenderTasks())
            .thenReturn(NetworkResult.Success(TaskListResponse(tasks)))

        with(vm) {

            fetchAllTasks()
            selectedDay = 1722004200000
            getSelectedDayTask()

            testDispatcher.scheduler.advanceUntilIdle()

            var dayAllTasks = vm.todayTaskList.value
            assertEquals("Selected Day Task Size", 2, dayAllTasks.size)
            assertEquals(
                "First task's title of selected day",
                "Dinner",
                dayAllTasks[0].taskDetail.title
            )
            assertEquals(
                "Second task's title of selected day",
                "Lunch",
                dayAllTasks[1].taskDetail.title
            )

            selectedDay = 1719815400000
            getSelectedDayTask()
            testDispatcher.scheduler.advanceUntilIdle()

            dayAllTasks = vm.todayTaskList.value
            assertEquals(1, dayAllTasks.size)
            assertNotEquals(
                "First task's title of selected day",
                "Dinner",
                dayAllTasks[0].taskDetail.title
            )

            selectedDay = 1722522600000
            getSelectedDayTask()
            testDispatcher.scheduler.advanceUntilIdle()

            dayAllTasks = vm.todayTaskList.value
            assertEquals(2, dayAllTasks.size)
            assertEquals(
                "First task's title of selected day",
                "Lunch",
                dayAllTasks[0].taskDetail.title
            )
            assertNotEquals(
                "Second task's title of selected day",
                "Lunch",
                dayAllTasks[1].taskDetail.title
            )
        }
    }

    @Test
    fun testClickPerform_AddAndDeleteTask() = runTest {
        Mockito.`when`(connectivity.isNetworkConnected())
            .thenReturn(true)

        Mockito.`when`(repo.fetchCalenderTasks())
            .thenReturn(NetworkResult.Success(TaskListResponse(tasks)))

        Mockito.`when`(repo.storeCalendarTask(StoreDataRequest(USER_ID, tasks[0].taskDetail)))
            .thenReturn(NetworkResult.Success(TaskStatusResponse("Success")))

        Mockito.`when`(repo.deleteTask(125))
            .thenReturn(NetworkResult.Success(TaskStatusResponse("Success")))

        vm.fetchAllTasks()
        vm.addTask("", "")
        vm.deleteTask(125)

        testDispatcher.scheduler.advanceUntilIdle()

        val allList = vm.allTasks

        assertEquals("All List Size", 4, allList.size)
        assertEquals(
            "After Delete the task, find that task ",
            null,
            allList.find { it.taskId == 125 })

    }

    @Test
    fun testClickPerform_clickForPreviousMonth() {
        val currentMonth = vm.currentShowingMonth.value

        vm.clickForPreviousMonth()
        vm.clickForPreviousMonth()
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedMonth = vm.currentShowingMonth.value
        assertEquals(
            "Current month - 2 => Previous Month  ",
            updatedMonth.second,
            currentMonth.second - 2
        )
    }

    @Test
    fun testClickPerform_clickForNextMonth() {
        val currentMonth = vm.currentShowingMonth.value

        vm.clickForNextMonth()
        vm.clickForNextMonth()
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedMonth = vm.currentShowingMonth.value
        assertEquals(
            "Current month + 2 => Next Month ",
            updatedMonth.second,
            currentMonth.second + 2
        )
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()

    }

}
