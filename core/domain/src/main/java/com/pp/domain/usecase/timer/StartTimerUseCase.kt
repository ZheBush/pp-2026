package com.pp.domain.usecase.timer

import com.pp.domain.controller.TimerController
import javax.inject.Inject

class StartTimerUseCase @Inject constructor(
    private val stopwatchController: TimerController
) {
    operator fun invoke(targetPackage: String) {
        stopwatchController.start(targetPackage)
    }
}