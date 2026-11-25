package com.pp.feature.grayscale.di.module

import com.pp.domain.controller.TimerController
import com.pp.impl.TimerControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimerModule {

    @Binds
    @Singleton
    abstract fun bindTimerController(
        impl: TimerControllerImpl
    ): TimerController
}