package com.pp.database.impl

import com.pp.database.AppStateRepository
import com.pp.database.dao.AppStateDao
import com.pp.database.entity.AppStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppStateRepositoryImpl @Inject constructor(
    private val appStateDao: AppStateDao
) : AppStateRepository {

    override fun observeIsEnabled(): Flow<Boolean> {
        return appStateDao.observeIsEnabled().map { it ?: false }
    }

    override suspend fun getIsEnabled(): Boolean {
        return appStateDao.getIsEnabled() ?: false
    }

    override suspend fun setEnabled(enabled: Boolean) {
        appStateDao.upsert(AppStateEntity(id = 1, isEnabled = enabled))
    }
}