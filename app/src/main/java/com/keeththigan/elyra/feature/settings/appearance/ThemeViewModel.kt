package com.keeththigan.elyra.feature.settings.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keeththigan.elyra.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(
    private val preferences: ThemePreferences
) : ViewModel() {

    private val _appearance =
        MutableStateFlow(preferences.getAppearance())

    val appearance: StateFlow<AppearanceOption> =
        _appearance.asStateFlow()

    fun setAppearance(
        option: AppearanceOption
    ) {
        preferences.setAppearance(option)
        _appearance.value = option
    }
}

class ThemeViewModelFactory(
    private val preferences: ThemePreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(preferences) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
