package com.pp.domain.usecase

import com.pp.domain.InterceptorController
import javax.inject.Inject

class DismissInterceptorUseCase @Inject constructor(
    private val interceptorController: InterceptorController
) {
    operator fun invoke() {
        interceptorController.dismiss()
    }
}