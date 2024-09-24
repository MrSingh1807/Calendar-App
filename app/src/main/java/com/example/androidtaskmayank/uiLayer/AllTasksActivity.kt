package com.example.androidtaskmayank.uiLayer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtaskmayank.databinding.ActivityAllTasksBinding
import com.example.androidtaskmayank.domainLayer.AllTasksVM
import com.example.androidtaskmayank.models.TaskListResponse
import com.example.androidtaskmayank.uiLayer.rvAdapters.DayTaskRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AllTasksActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAllTasksBinding.inflate(layoutInflater) }
    private val vm: AllTasksVM by viewModels()

    private val allTasksAdapter by lazy { DayTaskRVAdapter(::performLongPressTask) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            vm.taskList.collect { tasks ->
                Log.d("MrSingh", " Task Details Size : ${tasks.size} ")
                allTasksAdapter.setItems(tasks)
            }
        }

        lifecycleScope.launch {
            vm.toastMessage.collect { toastMessage ->
                Toast.makeText(this@AllTasksActivity, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }

        initAllTaskRV()
    }

    private fun initAllTaskRV() {
        binding.rvDailyTasks.layoutManager = LinearLayoutManager(this)
        binding.rvDailyTasks.adapter = allTasksAdapter
    }

    private fun performLongPressTask(taskId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Do you want to delete the task!")
            .setPositiveButton("Yes") { dialog, _ ->
                vm.deleteTask(taskId)
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }
}

