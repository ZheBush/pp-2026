package com.pp.database.di

import com.pp.database.AppConfigRepository
import com.pp.database.SessionManagerRepository
import com.pp.database.impl.AppConfigRepositoryImpl
import com.pp.database.impl.SessionManagerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(
        impl: AppConfigRepositoryImpl
    ): AppConfigRepository

    @Binds
    @Singleton
    abstract fun bindSessionManagerRepository(
        impl: SessionManagerRepositoryImpl
    ): SessionManagerRepository
}