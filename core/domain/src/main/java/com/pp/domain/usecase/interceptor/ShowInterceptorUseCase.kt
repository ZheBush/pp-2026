package com.pp.domain.usecase.interceptor

import com.pp.domain.controller.InterceptorController
import javax.inject.Inject

class ShowInterceptorUseCase @Inject constructor(
    private val interceptorController: InterceptorController
) {
    operator fun invoke(targetPackage: String) {
        interceptorController.show(targetPackage)
    }
}