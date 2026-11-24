package com.pp.domain.usecase

import com.pp.domain.InterceptorController
import javax.inject.Inject

class ShowInterceptorUseCase @Inject constructor(
    private val interceptorController: InterceptorController
) {
    operator fun invoke(targetPackage: String) {
        interceptorController.show(targetPackage)
    }
}