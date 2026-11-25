package com.pp.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pp.database.AppStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appStateRepository: AppStateRepository
) : ViewModel() {

    val isServiceEnabled: StateFlow<Boolean> = appStateRepository.observeIsEnabled()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun onToggleService(enabled: Boolean) {
        viewModelScope.launch {
            appStateRepository.setEnabled(enabled)
        }
    }
}