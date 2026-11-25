package com.pp.database.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pp.database.dao.AppStateDao
import com.pp.database.entity.AppStateEntity

@Database(
    entities = [AppStateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appStateDao(): AppStateDao
}