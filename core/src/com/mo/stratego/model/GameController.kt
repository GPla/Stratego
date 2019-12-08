package com.mo.stratego.model

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.communication.OnErrorEvent
import com.mo.stratego.model.player.Player
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.model.player.PlayerType
import com.mo.stratego.util.StateEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * This object handles the game flow and [GameState].
 * This class represents the referee mentioned in this paper,
 * see https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf
 */
object GameController {

    private lateinit var engine: Engine
    lateinit var players: Array<Player>
        private set
    var state: GameState = GameState.INIT_PLAYER_1
        private set
    lateinit var playerPieces: Map<Player, List<Piece>>
        private set

    /**
     * Init the object with this method. If not called before usage
     * an exception is thrown.
     * @param engine Engine
     * @param player1 Type of player 1
     * @param player2 Type of player 2
     */
    fun init(engine: Engine, player2: PlayerType): GameController {
        this.engine = engine

        // create players of given type
        players = arrayOf(
                PlayerType.createPlayer(PlayerType.LOCAL, PlayerId.PLAYER1),
                PlayerType.createPlayer(player2, PlayerId.PLAYER2))

        // create pieces for player
        playerPieces = mapOf(players[0] to PieceFactory.generateSet(players[0]),
                             players[1] to PieceFactory.generateSet(players[1]))

        StrategoGame.register(this)
        return this
    }


    /**
     * This method runs and updates the [GameState].
     * It should be called every frame by the game loop.
     */
    //FIXME: bug sometimes hangs in INIT_PLAYER_2
    //TODO: error handling
    fun run() {
        val result = when (state) {
            GameState.INIT_PLAYER_1        -> init(PlayerId.PLAYER1)
            GameState.INIT_PLAYER_2        -> init(PlayerId.PLAYER2)
            GameState.INIT_PREP_PLAYER_1   -> spawnPieces(players[0])
            GameState.PREPARATION_PLAYER_1 ->
                makePlayersPreparation(PlayerId.PLAYER1)
            GameState.INIT_PREP_PLAYER_2   -> spawnPieces(players[1])
            GameState.PREPARATION_PLAYER_2 ->
                makePlayersPreparation(PlayerId.PLAYER2)
            GameState.GAME_START           -> getFirstTurn()
            GameState.TURN_PLAYER_1        -> makePlayersTurn(0)
            GameState.TURN_PLAYER_2        -> makePlayersTurn(1)
            GameState.GAME_OVER            -> false
        }

        // goto next state
        if (result) {
            state++
            // post state change to hudcontroller
            state.title?.run {
                EventBus.getDefault().post(StateEvent(this))
            }
        }
    }

    /**
     * Evaluate who's first.
     * @return True
     */
    // TODO: handle error state
    private fun getFirstTurn(): Boolean {
        val player1Num =
                players[PlayerId.PLAYER1.id].startNumber?.number ?: return false
        val player2Num =
                players[PlayerId.PLAYER2.id].startNumber?.number ?: return false

        if (player2Num > player1Num) {
            state = GameState.TURN_PLAYER_1
            return true
        }
        return true
    }

    /**
     * Init state.
     * Exchange starting numbers.
     */
    private fun init(playerId: PlayerId): Boolean {
        val id = playerId.id

        return players[id].startNumber?.run {
            val otherId = (id + 1) % players.size
            players[otherId].processOthersStartingNumber(this)

            true
        } ?: false
    }

    /**
     * Make players turn.
     * @param id Id of active player
     * @return True if the move is completed.
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
        players[otherId].presentOthersMove()

        return true
    }

    /**
     * Make [Player]s preparation.
     * @param playerId Id of the [Player]
     * @return True if the preparation is completed.
     */
    private fun makePlayersPreparation(playerId: PlayerId): Boolean {
        val id = playerId.id

        // allow player to move pieces
        players[id].allow = true

        // all pieces are placed
        val grid = players[id].startingGrid ?: return false
        if (!grid.isValid())
            return false

        players[id].allow = false
        players[id].presentGrid(playerPieces.getValue(players[id]))

        // process the other players grid
        val otherId = (id + 1) % players.size
        players[otherId].processOthersGrid(grid)

        return true
    }

    /**
     * Adds [Piece]s for the given [Player] to the engine.
     * @param player Player
     */
    private fun spawnPieces(player: Player): Boolean {
        for (piece in playerPieces.getValue(player)) {
            engine.addEntity(piece)
        }
        return true
    }

    /**
     * This method is called when a player won the game.
     * The game freezes and a popup is displayed.
     * @param player Player that won the game
     */
    // TODO sent event to hud
    fun win(player: Player) {
        Gdx.app.log("dtag", "player ${player.id} won the game!")
        state = GameState.GAME_OVER
    }

    /**
     * TODO
     * Returns to [MainMenuScreen] if an error occurs.
     * @param event
     */
    //FIXME: switch back does not work 
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onErrorEvent(event: OnErrorEvent) {
        Gdx.app.log("bth", "connection lost: main menu")
        StrategoGame.screen.dispose()
        StrategoGame.screen = MainMenuScreen()
    }
}