package com.keeththigan.elyra.data.preferences

import android.content.Context
import com.keeththigan.elyra.feature.settings.appearance.AppearanceOption

/**
 * Persists the user's appearance preference across app restarts.
 */
class ThemePreferences(
    context: Context
) {

    private val prefs =
        context.applicationContext.getSharedPreferences(
            "elyra_theme",
            Context.MODE_PRIVATE
        )

    fun getAppearance(): AppearanceOption {

        val stored =
            prefs.getString(KEY_APPEARANCE, null)
                ?: return AppearanceOption.SYSTEM

        return runCatching {
            AppearanceOption.valueOf(stored)
        }.getOrDefault(AppearanceOption.SYSTEM)
    }

    fun setAppearance(
        option: AppearanceOption
    ) {
        prefs.edit()
            .putString(KEY_APPEARANCE, option.name)
            .apply()
    }

    private companion object {
        const val KEY_APPEARANCE = "appearance"
    }
}
