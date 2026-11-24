package com.pp.common

import android.content.Context
import androidx.core.content.edit

object InterceptorPrefs {
    private var bypassedPackage: String? = null

    fun allowOnce(packageName: String) {
        bypassedPackage = packageName
    }

    fun isBypassed(packageName: String): Boolean {
        return bypassedPackage == packageName
    }

    fun clearIfDifferent(currentPackage: String) {
        if (bypassedPackage != null && bypassedPackage != currentPackage) {
            bypassedPackage = null
        }
    }

    fun clear() {
        bypassedPackage = null
    }
}