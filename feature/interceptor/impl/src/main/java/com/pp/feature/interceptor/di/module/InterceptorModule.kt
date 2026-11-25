package com.pp.feature.interceptor.di.module

import com.pp.domain.controller.InterceptorController
import com.pp.feature.interceptor.impl.InterceptorControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterceptorModule {

    @Binds
    @Singleton
    abstract fun bindInterceptorController(
        impl: InterceptorControllerImpl
    ): InterceptorController
}