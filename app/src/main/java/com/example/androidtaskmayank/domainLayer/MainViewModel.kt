package com.example.androidtaskmayank.domainLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtaskmayank.models.StoreDataRequest
import com.example.androidtaskmayank.models.Task
import com.example.androidtaskmayank.models.TaskDetail
import com.example.androidtaskmayank.repositories.TasksRepo
import com.example.androidtaskmayank.utils.CalenderUtils
import com.example.androidtaskmayank.utils.NetworkConnectivity
import com.example.androidtaskmayank.utils.NetworkResult
import com.example.androidtaskmayank.utils.NetworkUtil
import com.example.androidtaskmayank.utils.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivity: NetworkConnectivity,
    private val tasksRepo: TasksRepo
) : ViewModel() {

    var allTasks = listOf<Task>()
    private val calendar = CalenderUtils()
    private val today = calendar.getTodayDate()

    private val mutableTodayTaskList = MutableStateFlow(emptyList<Task>())
    val todayTaskList = mutableTodayTaskList.asStateFlow()

    private val mutableCurrentShowingMonth =
        MutableStateFlow(today.first to today.second)
    val currentShowingMonth = mutableCurrentShowingMonth.asStateFlow()

    private val mutableToastMessage = MutableStateFlow<String?>(null)
    val toastMessage = mutableToastMessage.filterNotNull()

    private var year = today.first
    private var month = today.second
    private var currentDay = today.third

    var selectedDay: Long = dateInMillie(currentDay)

    init {
        fetchAllTasks()
    }

    fun isSelectedDateCurrentDateFutureDate(selection: Long = selectedDay): Boolean {
        val currentDate = calendar.extractDateTimeFromTimestamp(System.currentTimeMillis())
        val selectedDate = calendar.extractDateTimeFromTimestamp(selection)

        return when {
            selectedDate.year > currentDate.year -> true
            selectedDate.year == currentDate.year && selectedDate.monthValue > currentDate.monthValue -> true
            selectedDate.year == currentDate.year && selectedDate.monthValue == currentDate.monthValue && selectedDate.dayOfMonth >= currentDate.dayOfMonth -> true
            else -> false
        }
    }

    fun extractSelectionDate(): Int {
        val date = calendar.extractDateTimeFromTimestamp(selectedDay)
        return if (date.year == year && date.monthValue == month) date.dayOfMonth else -1
    }

    fun getSelectedDayTask() {
        val selectedDateTasks = allTasks.filter { task ->
            calendar.compareTwoDatesIsSame(task.taskDetail.date, selectedDay)
        }
        viewModelScope.launch {
            mutableTodayTaskList.emit(selectedDateTasks.sortedBy { it.taskDetail.date })
        }
    }

    fun fetchAllTasks() {
        if (connectivity.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = tasksRepo.fetchCalenderTasks()) {
                    is NetworkResult.Error -> {}
                    is NetworkResult.Success -> {
                        response.data?.let {
                            allTasks = it.tasks
                        }
                        getSelectedDayTask()
                    }
                }
            }
        }

    }

    fun dateInMillie(selectedDate: Int) = calendar.dateToMillis(year, month, selectedDate)

    fun addTask(title: String, description: String) {
        if (connectivity.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                val taskDetails = TaskDetail(selectedDay, description, title)
                val storeDataRequest = StoreDataRequest(USER_ID, taskDetails)

                when (val response =
                    tasksRepo.storeCalendarTask(storeDataRequest = storeDataRequest)) {
                    is NetworkResult.Error -> mutableToastMessage.emit(response.message)
                    is NetworkResult.Success -> {
                        mutableToastMessage.emit("Task Added Successfully")
                        fetchAllTasks()
                    }
                }
            }
        }
    }

    fun firstDayInMonth(): Int {
        val dayOfWeek = calendar.getFirstDayOfMonth(year, month)
        return DayOfWeek.entries.indexOf(dayOfWeek)
    }

    fun daysInMonth(): Int {
        return calendar.getTotalDaysInMonth(year, month)
    }

    fun clickForPreviousMonth() {
        if (month == 1) {
            year--
            month = 12
        } else month--

        viewModelScope.launch {
            mutableCurrentShowingMonth.emit(year to month)
        }
    }

    fun clickForNextMonth() {
        if (month == 12) {
            year++
            month = 1
        } else month++
        viewModelScope.launch {
            mutableCurrentShowingMonth.emit(year to month)
        }
    }

    fun deleteTask(taskId: Int) {
        if (connectivity.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = tasksRepo.deleteTask(taskId)) {
                    is NetworkResult.Error -> mutableToastMessage.emit(response.message)
                    is NetworkResult.Success -> {
                        mutableToastMessage.emit("Task Deleted Successfully")

                        val updatedList = allTasks.filter { it.taskId != taskId }
                        allTasks = updatedList
                        getSelectedDayTask()
                    }
                }
            }
        }
    }

}
