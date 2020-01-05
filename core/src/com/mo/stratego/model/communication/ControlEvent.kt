package com.mo.stratego.model.communication

import com.mo.stratego.model.game.GameController
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor

/**
 * Enum of control events. For communication between
 * the [GameController]s.
 */
enum class ControlEvent {
    OFFER_DRAW,
    ACCEPT_DRAW,
    SURRENDER;

    /**
     * Serializer for [ControlEvent]. Adapted from
     * https://ahsensaeed.com/enum-class-serialization-kotlinx-serialization-library/
     */
    @Serializer(forClass = ControlEvent::class)
    companion object : KSerializer<ControlEvent> {
        override val descriptor: SerialDescriptor
            get() = StringDescriptor

        override fun deserialize(decoder: Decoder): ControlEvent {
            return valueOf(decoder.decodeString().toUpperCase())
        }

        override fun serialize(encoder: Encoder, obj: ControlEvent) {
            encoder.encodeString(obj.name.toLowerCase())
        }

    }
}