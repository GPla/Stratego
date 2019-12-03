package com.mo.stratego.model.player

import kotlinx.serialization.Serializable


/**
 * Starting number that determines the starting order.
 * @property number Number
 */
@Serializable
data class StartNumber(val number: Int) {
    // for serialization
    private val className = StartNumber::class.java.name
}