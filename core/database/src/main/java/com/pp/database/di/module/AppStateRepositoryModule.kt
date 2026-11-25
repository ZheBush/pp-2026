package com.pp.database.di.module

import com.pp.database.AppStateRepository
import com.pp.database.impl.AppStateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppStateRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppStateRepository(
        impl: AppStateRepositoryImpl
    ): AppStateRepository
}