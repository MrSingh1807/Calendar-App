package com.example.androidtaskmayank.uiLayer.rvAdapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.androidtaskmayank.R
import com.example.androidtaskmayank.baseClasses.BaseRecyclerAdapter
import com.example.androidtaskmayank.databinding.LayoutDateBinding

class DateRVAdapter(
    private val selectedDate: (date: Int) -> Unit
) : BaseRecyclerAdapter<Int, LayoutDateBinding>() {

    private var firstDayWeek = 0
    private var currentSelection = -1

    override fun bindingInflater(parent: ViewGroup) =
        LayoutDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: LayoutDateBinding, item: Int, position: Int) {
        with(binding) {
            tvDate.text = if (item == 0) "" else item.toString()

            if (position == currentSelection) {
                root.setBackgroundResource(R.drawable.selected_date)
                tvDate.setTextColor(Color.WHITE)
            } else {
                root.setBackgroundColor(Color.TRANSPARENT)
                tvDate.setTextColor(Color.BLACK)
            }

            root.setOnClickListener {
                if (item != 0) {
                    notifyItemChanged(currentSelection)
                    currentSelection = position
                    notifyItemChanged(currentSelection)
                    selectedDate(item)
                }
            }
        }
    }


    fun updateMonth(firstDayOfMonth: Int, items: Int, defaultSelection: Int) {
        this.firstDayWeek = firstDayOfMonth
        this.setItems(mutableListOf<Int>().also {
            if (firstDayWeek > 0) {
                for (i in 1..firstDayWeek) {
                    it.add(0)
                }
            }
            it.addAll((1..items))
        })
        currentSelection = if (defaultSelection > 0) firstDayOfMonth - 1 + defaultSelection else -1

    }

}
