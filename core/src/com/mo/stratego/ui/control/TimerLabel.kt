package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin

/**
 * Label, that shows the time since rendered first.
 */
class TimerLabel(skin: Skin) : Label("Time: 00:00", skin) {
    private var lastTick: Float = 0f
    var seconds: Int = 0
    var minutes: Int = 0


    override fun act(delta: Float) {
        lastTick += delta

        // delta is in seconds
        // count up every second
        if (lastTick >= 1f) {
            seconds++
            lastTick = 0f
            if (seconds >= 60) {
                minutes++
                seconds = 0
            }
        }

        setText("Time: ${minutes.toString().padStart(2, '0')}" +
                ":${seconds.toString().padStart(2, '0')}")
    }
}