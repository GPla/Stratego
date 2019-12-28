package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component

/**
 * Delays the execution of other Components. The executing system must exclude
 * entities with the [WaitComponent]. After the wait time is elapsed, the
 * WaitComponent is removed and the execution of other components can continue.
 *
 * @property waitTime Time to wait
 */
class WaitComponent(val waitTime: Float) : Component {
    var time: Float = 0f
}