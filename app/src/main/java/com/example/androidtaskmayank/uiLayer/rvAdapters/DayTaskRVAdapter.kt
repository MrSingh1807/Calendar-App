package com.example.androidtaskmayank.uiLayer.rvAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidtaskmayank.baseClasses.BaseRecyclerAdapter
import com.example.androidtaskmayank.databinding.LayoutDailyTaskBinding
import com.example.androidtaskmayank.models.Task
import com.example.androidtaskmayank.utils.CalenderUtils.Companion.formatTimeMillisToString


class DayTaskRVAdapter(
    private val onLongPressItem: (taskId: Int) -> Unit
) : BaseRecyclerAdapter<Task, LayoutDailyTaskBinding>() {

    override fun bindingInflater(parent: ViewGroup) =
        LayoutDailyTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: LayoutDailyTaskBinding, item: Task, position: Int) {
        with(binding) {
            val taskDetail = item.taskDetail
            tvTitle.text = taskDetail.title
            tvShowDescription.text = taskDetail.description
            try {
                tvDate.text = formatTimeMillisToString(taskDetail.date, "dd/MM/yyyy")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            root.setOnLongClickListener {
                onLongPressItem(item.taskId)
                true
            }
        }
    }

}
