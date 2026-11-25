package com.pp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pp.database.entity.AppStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {

    @Query("SELECT isEnabled FROM app_state WHERE id = 1")
    fun observeIsEnabled(): Flow<Boolean?>

    @Query("SELECT isEnabled FROM app_state WHERE id = 1")
    suspend fun getIsEnabled(): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: AppStateEntity)
}