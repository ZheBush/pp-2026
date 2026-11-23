package com.pp.model

data class AppSession(
    val packageName: String,
    val startTime: Long,
    val endTime: Long?,
    val duration: Long
)
