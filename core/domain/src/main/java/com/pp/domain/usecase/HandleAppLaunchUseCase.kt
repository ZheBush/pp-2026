package com.pp.domain.usecase

import com.pp.database.AppConfigRepository
import com.pp.database.SessionManagerRepository
import com.pp.domain.AppLaunchActions
import javax.inject.Inject

class HandleAppLaunchUseCase @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
    private val sessionManagerRepository: SessionManagerRepository
) {
    suspend operator fun invoke(packageName: String): AppLaunchActions? {
        val config = appConfigRepository.getAppConfig(packageName) ?: return null
        if (!config.isSelected) return null

        sessionManagerRepository.startActiveSession(packageName)

        return AppLaunchActions(
            shouldShowInterceptor = config.isInterceptorEnabled,
            shouldEnableGrayscale = config.isGrayscaleEnabled
        )
    }
}