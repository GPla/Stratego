package com.mo.stratego.model

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.mo.stratego.model.player.Player
import com.mo.stratego.model.player.PlayerType

/**
 * This object handles the game flow and [GameState].
 * It represents the referee mentioned in this paper,
 * see https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf
 */
object GameController {

    private lateinit var engine: Engine
    lateinit var players: Array<Player>
    var state: GameState = GameState.TURN_PLAYER_1
        private set

    /**
     * Init the object with this method. If not called before usage
     * an exception is thrown.
     * @param engine Engine
     * @param player1 Type of player 1
     * @param player2 Type of player 2
     */
    fun init(engine: Engine, player1: PlayerType,
             player2: PlayerType): GameController {
        this.engine = engine
        players = arrayOf(PlayerType.createPlayer(player1, 0),
                          PlayerType.createPlayer(player2, 1))
        return this
    }


    /**
     * This method runs and updates the [GameState].
     * It should be called every frame by the game loop.
     */
    //TODO: starting player has to be synced with other device
    //TODO: show who's turn
    //TODO: !! make event based?
    fun run() {
        val result = when (state) {
            GameState.TURN_PLAYER_1 -> makePlayersTurn(0)
            GameState.TURN_PLAYER_2 -> makePlayersTurn(1)
            else                    -> false
        }

        // goto next state
        if (result)
            state++
    }

    /**
     * Make players turn.
     * @param id Id of active player
     */
    private fun makePlayersTurn(id: Int): Boolean {
        // id out of bound
        if (id >= players.size)
            return false

        // allow player to make move
        players[id].allow = true

        // get players move and put into the other player
        val otherId = (id + 1) % players.size
        players[otherId].othersMove = players[id].move ?: return false
        players[id].move = null

        // players move is over
        players[id].allow = false

        // present move to gui
        players[otherId].present()

        return true
    }

    /**
     * This method is called when a player won the game.
     * The game freezes and a popup is displayed.
     * @param player Player that won the game
     */
    fun win(player: Player) {
        Gdx.app.log("dtag", "player ${player.id} won the game!")
        state = GameState.GAME_OVER
    }


}