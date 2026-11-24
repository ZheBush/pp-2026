package com.pp.domain.di

import com.pp.database.AppConfigRepository
import com.pp.database.SessionManagerRepository
import com.pp.domain.InterceptorController
import com.pp.domain.usecase.DismissInterceptorUseCase
import com.pp.domain.usecase.HandleAppExitUseCase
import com.pp.domain.usecase.HandleAppLaunchUseCase
import com.pp.domain.usecase.ShowInterceptorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideHandleAppLaunchUseCase(
        appConfigRepository: AppConfigRepository,
        sessionManagerRepository: SessionManagerRepository
    ): HandleAppLaunchUseCase = HandleAppLaunchUseCase(
        appConfigRepository,
        sessionManagerRepository
    )

    @Provides
    fun provideHandleAppExitUseCase(
        sessionManagerRepository: SessionManagerRepository
    ): HandleAppExitUseCase = HandleAppExitUseCase(
        sessionManagerRepository
    )
}