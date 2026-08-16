package com.keeththigan.elyra.data.model

import com.google.firebase.firestore.PropertyName

/**
 * One individually addressable switch inside a multi-switch gang box.
 *
 * The name is what makes a gang box usable — "Ceiling Fan" and "Counter
 * Light" rather than "Switch 1" and "Switch 2" — so it is set by the user
 * when the unit is added and editable afterwards.
 */
data class SwitchChannel(
    val index: Int = 0,
    val name: String = "",

    /** @PropertyName is required for the same reason as [Device.isOn]. */
    @get:PropertyName("isOn")
    @set:PropertyName("isOn")
    var isOn: Boolean = false
) {

    /** Falls back to a positional label only when unnamed. */
    fun displayName(): String =
        name.ifBlank { "Switch $index" }
}
