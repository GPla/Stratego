package com.mo.stratego.model

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.mo.stratego.model.player.Player
import com.mo.stratego.model.player.PlayerType

/**
 * TODO: add description
 */
object GameController {

    private lateinit var engine: Engine

    lateinit var players: Array<Player> // array of players

    /**
     * Init the object with this method. If not called before usage
     * an exception is thrown.
     */
    fun init(engine: Engine, player1: PlayerType,
             player2: PlayerType): GameController {
        this.engine = engine
        players = arrayOf(PlayerType.createPlayer(player1, 0),
                          PlayerType.createPlayer(player2, 1))
        return this
    }

    //TODO: do something
    fun win(player: Player) {
        Gdx.app.log("dtag", "player ${player.id} won the game!")
    }


}