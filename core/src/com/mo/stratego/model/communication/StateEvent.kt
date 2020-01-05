package com.mo.stratego.model.communication

import com.mo.stratego.model.game.GameController

/**
 * [GameController] state change event.
 *
 * @property state New state title
 * @property turn Current turn
 */
data class StateEvent(val state: String, val turn: Int?)
