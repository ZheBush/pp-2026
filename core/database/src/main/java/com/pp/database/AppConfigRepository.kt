package com.pp.database

import com.pp.model.TargetAppConfig
import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    suspend fun getAppConfig(packageName: String): TargetAppConfig?
    fun observeMonitoredApps(): Flow<List<TargetAppConfig>>
}