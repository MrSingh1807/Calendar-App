package com.example.androidtaskmayank.uiLayer.customDialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.androidtaskmayank.R
import com.example.androidtaskmayank.databinding.LayoutAddTaskBinding
import com.example.androidtaskmayank.utils.CalenderUtils.Companion.formatTimeMillisToString


class AddTaskDialog(
    private val mContext: Context,
    private val date: Long,
    private val addTask: (title: String, desc: String) -> Unit
) : Dialog(mContext, R.style.CustomDialog) {

    private val binding by lazy { LayoutAddTaskBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val lp = WindowManager.LayoutParams().apply {
            copyFrom(window?.attributes)
            width = WindowManager.LayoutParams.MATCH_PARENT
        }

        with(binding) {

            tvDate.text = formatTimeMillisToString(date, "dd/MM/yyyy")

            btnCancel.setOnClickListener { dismiss() }

            btnSubmit.setOnClickListener {

                etTitle.text.ifBlank {
                    Toast.makeText(
                        mContext, "Please give a suitable title of your task",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }
                etShowDescription.text.ifBlank {
                    Toast.makeText(
                        mContext, "Please give a suitable description of your task",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@setOnClickListener
                }

                addTask(etTitle.text.toString(), etShowDescription.text.toString())
                dismiss()
            }
        }

        setCancelable(true)

        window?.setAttributes(lp);
    }

}