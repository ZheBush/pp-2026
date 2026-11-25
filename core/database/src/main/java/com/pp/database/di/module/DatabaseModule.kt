package com.pp.database.di.module

import android.content.Context
import androidx.room.Room
import com.pp.database.dao.AppStateDao
import com.pp.database.di.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "digital_wellbeing.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppStateDao(db: AppDatabase): AppStateDao {
        return db.appStateDao()
    }
}