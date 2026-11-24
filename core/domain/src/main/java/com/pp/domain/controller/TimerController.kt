package com.pp.domain.controller

interface TimerController {
    fun start(targetPackage: String)
    fun stop()
}