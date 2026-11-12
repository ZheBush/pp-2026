package com.pp.domain.usecase

import com.pp.database.SessionManagerRepository
import javax.inject.Inject

class HandleAppExitUseCase @Inject constructor(
    private val sessionManagerRepository: SessionManagerRepository
) {
    suspend operator fun invoke() {
        sessionManagerRepository.endActiveSession()
    }
}