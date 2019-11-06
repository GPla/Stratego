package com.mo.stratego.model

import com.badlogic.ashley.core.Engine
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.player.Player

/**
 * TODO: add description
 */
object GameController {

    private lateinit var engine : Engine

    lateinit var players : Array<Player> // array of players

    /**
     * Init the object with this method. If not called before usage
     * an exception is thrown.
     */
    fun init(engine : Engine, player1: Player, player2 : Player) : GameController{
        this.engine = engine
        players = arrayOf(player1, player2)
        return this
    }




}