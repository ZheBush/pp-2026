package com.pp.domain.di.module

import com.pp.database.AppConfigRepository
import com.pp.database.SessionManagerRepository
import com.pp.domain.usecase.HandleAppExitUseCase
import com.pp.domain.usecase.HandleAppLaunchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HandleAppModule {

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