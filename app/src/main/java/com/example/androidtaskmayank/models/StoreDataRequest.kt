package com.example.androidtaskmayank.models

import com.example.androidtaskmayank.utils.USER_ID
import com.google.gson.annotations.SerializedName

data class StoreDataRequest(
    @SerializedName("user_id") val userId: Int = USER_ID,
    @SerializedName("task") val task: TaskDetail
)

data class AboutTask(
    val title: String,
    val description: String,
)
