package com.pp.domain.usecase.interceptor

import com.pp.domain.controller.InterceptorController
import javax.inject.Inject

class DismissInterceptorUseCase @Inject constructor(
    private val interceptorController: InterceptorController
) {
    operator fun invoke() {
        interceptorController.dismiss()
    }
}