package com.pp.domain.usecase.grayscale

import com.pp.domain.controller.GrayscaleController
import javax.inject.Inject

class SetGrayscaleUseCase @Inject constructor(
    private val grayscaleController: GrayscaleController
) {
    operator fun invoke(enabled: Boolean) {
        grayscaleController.setGrayscaleEnabled(enabled)
    }
}