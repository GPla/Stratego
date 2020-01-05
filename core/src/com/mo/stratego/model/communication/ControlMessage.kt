package com.mo.stratego.model.communication

import kotlinx.serialization.Serializable

/**
 * Wrapper for [ControlEvent]. Enum cannot directly be serialized with KotlinX.
 * @property event Event
 */
@Serializable
class ControlMessage(val event: ControlEvent) {
    // for serialization
    private val className = ControlMessage::class.java.name
}