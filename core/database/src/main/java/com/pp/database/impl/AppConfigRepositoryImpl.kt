package com.pp.database.impl

import com.pp.database.AppConfigRepository
import com.pp.model.TargetAppConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigRepositoryImpl @Inject constructor() : AppConfigRepository {

    private val mutex = Mutex()

    private val configsFlow = MutableStateFlow(
        mapOf(
            "com.instagram.android" to TargetAppConfig(
                packageName = "com.instagram.android",
                appName = "Instagram",
                isSelected = true,
                isInterceptorEnabled = true,
                isTimerEnabled = true,
                isGrayscaleEnabled = false
            )
        )
    )

    override suspend fun getAppConfig(packageName: String): TargetAppConfig? {
        return mutex.withLock {
            configsFlow.value[packageName]
        }
    }

    override fun observeMonitoredApps(): Flow<List<TargetAppConfig>> {
        return configsFlow.asStateFlow().map { configs ->
            configs.values.filter { it.isSelected }
        }
    }
}