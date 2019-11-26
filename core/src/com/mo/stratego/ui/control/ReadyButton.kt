package com.mo.stratego.ui.control

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.mo.stratego.model.GameController
import com.mo.stratego.model.GameState
import com.mo.stratego.model.Rank
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.map.StartingGrid
import com.mo.stratego.util.Constants


/**
 * This Button's job is to confirm that the active player's preparation is done.
 */
class ReadyButton(skin: Skin) : TextButton("Ready!", skin), EventListener {
    init {
        addListener(this)
        setPosition(Constants.getUnitToPixel(8.4f),
                    Constants.getUnitToPixel(8.8f))
        isTransform = true
    }

    override fun act(delta: Float) {
        isVisible = GameController.state.let {
            // only visible in prep state
            if (it in arrayOf(GameState.PREPARATION_PLAYER_1,
                              GameState.PREPARATION_PLAYER_2))
            // check if all pieces are on the grid
                it.activePlayerId?.let {
                    Grid.spawnMap.values.map { x -> x[it.id] }.sum() == 0
                } ?: false
            else
                false
        }
        super.act(delta)
    }

    /**
     * Handle click event. Get starting grid for active player.
     * @param event Event
     * @return Whether or not the event is handled.
     */
    override fun handle(event: Event?): Boolean {
        if ((event as? InputEvent)?.type != InputEvent.Type.touchDown)
            return true

        val sGrid = StartingGrid()
        //sGrid.forEachIndexedSet { x, y -> Grid[x, y]?.rank ?: return false }
        sGrid.forEachIndexedSet { x, y -> Grid[x, y]?.rank ?: Rank.FLAG }

        // set starting grid for player
        GameController.state.activePlayerId?.let {
            GameController.players[it.id].startingGrid = sGrid
        }

        return true
    }

}