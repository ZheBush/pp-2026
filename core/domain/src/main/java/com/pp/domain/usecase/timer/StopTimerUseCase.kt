package com.pp.domain.usecase.timer

import com.pp.domain.controller.TimerController
import javax.inject.Inject

class StopTimerUseCase @Inject constructor(
    private val stopTimerController: TimerController
) {
    operator fun invoke() {
        stopTimerController.stop()
    }
}