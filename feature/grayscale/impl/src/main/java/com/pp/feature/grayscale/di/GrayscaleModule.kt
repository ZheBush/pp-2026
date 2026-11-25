package com.pp.feature.grayscale.di

import com.pp.domain.controller.GrayscaleController
import com.pp.feature.grayscale.impl.GrayscaleControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GrayscaleModule {

    @Binds
    @Singleton
    abstract fun bindGrayscaleController(
        impl: GrayscaleControllerImpl
    ): GrayscaleController
}