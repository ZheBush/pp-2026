package com.pp.database.impl

import com.pp.database.SessionManagerRepository
import com.pp.model.AppSession
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerRepositoryImpl @Inject constructor() : SessionManagerRepository {

    private val mutex = Mutex()
    private var activeSession: AppSession? = null
    private var lastCompletedSession: AppSession? = null

    override suspend fun startActiveSession(packageName: String) {
        mutex.withLock {
            val now = System.currentTimeMillis()

            activeSession?.let { previous ->
                if (previous.packageName == packageName && previous.endTime == null) {
                    return@withLock
                }
                lastCompletedSession = previous.finalizeAt(now)
            }

            activeSession = AppSession(
                packageName = packageName,
                startTime = now,
                endTime = null,
                duration = 0L
            )
        }
    }

    override suspend fun endActiveSession() {
        mutex.withLock {
            val current = activeSession ?: return@withLock
            val now = System.currentTimeMillis()
            lastCompletedSession = current.finalizeAt(now)
            activeSession = null
        }
    }

    override suspend fun getActiveSession(): AppSession? {
        return mutex.withLock {
            activeSession
        }
    }

    private fun AppSession.finalizeAt(endTimestamp: Long): AppSession {
        val safeEnd = endTimestamp.coerceAtLeast(startTime)
        return copy(
            endTime = safeEnd,
            duration = safeEnd - startTime
        )
    }
}