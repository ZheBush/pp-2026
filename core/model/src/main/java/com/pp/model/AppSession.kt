package com.pp.model

data class AppSession(
    val id: Long,
    val packageName: String,
    val startTime: Long,
    val endTime: Long? = null,
    val duration: Long = 0
)
