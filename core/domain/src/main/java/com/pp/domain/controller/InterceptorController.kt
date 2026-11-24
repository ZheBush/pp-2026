package com.pp.domain.controller

interface InterceptorController {
    fun show(targetPackage: String)
    fun dismiss()
}