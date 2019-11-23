package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mo.stratego.model.Rank
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.util.Constants

/**
 * Custom label class, that display the count of stacked pieces.
 */
class CounterLabel(val rank: Rank, val ownerId: PlayerId, style: Skin)
    : Label("0x", style) {

    init {
        val position = rank.getDefaultPosition(ownerId)
        setPosition(Constants.getUnitToPixel(position.x + 0.6f),
                    Constants.getUnitToPixel(position.y + 0.7f))
        setFontScale(1.3f)
        setColor(1f, 1f, 1f, 1f)
    }

    var counter: Int = 0

    override fun act(delta: Float) {
        counter = Grid.spawnMap.getValue(rank)[ownerId.id]
        isVisible = counter > 0
        setText("${counter}x")
        super.act(delta)
    }

}