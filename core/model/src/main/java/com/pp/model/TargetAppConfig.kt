package com.pp.model

data class TargetAppConfig(
    val packageName: String,
    val appName: String,
    val isSelected: Boolean,
    val isInterceptorEnabled: Boolean,
    val isTimerEnabled: Boolean,
    val isGrayscaleEnabled: Boolean
)