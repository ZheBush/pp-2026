package com.pp.database

import com.pp.database.dao.AppStateDao
import com.pp.database.entity.AppStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface AppStateRepository {
    fun observeIsEnabled(): Flow<Boolean>
    suspend fun getIsEnabled(): Boolean
    suspend fun setEnabled(enabled: Boolean)
}