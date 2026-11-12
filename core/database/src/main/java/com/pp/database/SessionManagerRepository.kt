package com.pp.database

import com.pp.model.AppSession

interface SessionManagerRepository {
    suspend fun startActiveSession(packageName: String)
    suspend fun endActiveSession()
    suspend fun getActiveSession(): AppSession?
}