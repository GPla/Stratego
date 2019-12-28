package com.mo.stratego.model

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.MainMenuScreen
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.communication.*
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.player.Player
import com.mo.stratego.model.player.PlayerId
import com.mo.stratego.model.player.PlayerProxy
import com.mo.stratego.model.player.PlayerType
import com.mo.stratego.ui.Screens
import com.mo.stratego.ui.controller.HudController
import com.mo.stratego.ui.provider.DialogProvider
import com.mo.stratego.util.Constants
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
    private var turnCounter = TurnCounter()

    /**
     * Contains the move history for the current game.
     */
    val moveHistory: MutableMap<PlayerId, MutableList<Move>> =
            mutableMapOf(PlayerId.PLAYER1 to mutableListOf(),
                         PlayerId.PLAYER2 to mutableListOf())

    //TODO remove debug
    private fun setupForDebug(playerId: PlayerId): Boolean {
        val p1 = playerPieces[players[0]]
        val p2 = playerPieces[players[1]]

        // player 1
        if (playerId == PlayerId.PLAYER1) {

            var y = 4
            p1?.forEachIndexed { index, piece ->
                if ((index % 10 == 0))
                    y++
                piece.add(MoveComponent(GridPoint2(index % 10, y - 1),
                                        MoveType.ABSOLUTE))
            }
        } else {
            // player 2
            var y = 10
            p2?.forEachIndexed { index, piece ->
                if (index % 10 == 0)
                    y++
                piece.add(MoveComponent(GridPoint2(index % 10, y - 1),
                                        MoveType.ABSOLUTE))
            }
        }
        return true
    }

    /**
     * Init the object with this method. If not called before usage
     * an exception is thrown.
     * @param engine Engine
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

        // register for events on event bus
        StrategoGame.register(this)

        // reset for new game
        turnCounter = TurnCounter()
        state = GameState.INIT_PLAYER_1
        moveHistory[PlayerId.PLAYER1]!!.clear()
        moveHistory[PlayerId.PLAYER2]!!.clear()


        return this
    }

    /**
     * This method runs and updates the [GameState].
     * It should be called every frame by the game loop.
     */
    fun run() {
        val result = when (state) {
            GameState.INIT_PLAYER_1        -> init(PlayerId.PLAYER1)
            GameState.INIT_PLAYER_2        -> init(PlayerId.PLAYER2)
            GameState.INIT_PREP_PLAYER_1   -> spawnPieces(players[0])
            GameState.PREPARATION_PLAYER_1 -> //setupForDebug(PlayerId.PLAYER1)
                makePlayersPreparation(PlayerId.PLAYER1)
            GameState.INIT_PREP_PLAYER_2   -> spawnPieces(players[1])
            GameState.PREPARATION_PLAYER_2 -> //setupForDebug(PlayerId.PLAYER2)
                makePlayersPreparation(PlayerId.PLAYER2)
            GameState.GAME_START           -> getFirstTurn()
            GameState.TURN_PLAYER_1        -> makePlayersTurn(PlayerId.PLAYER1)
            GameState.TURN_PLAYER_2        -> makePlayersTurn(PlayerId.PLAYER2)
            GameState.GAME_OVER            -> false
        }

        // goto next state
        if (result) {
            // update state and counter
            state++
            turnCounter.changedState(state)

            // post state change to hud controller
            state.title?.run {
                EventBus.getDefault()
                        .post(StateEvent(this, turnCounter.counter))
            }
        }
    }

    /**
     * Evaluate who's first. The player with the higher starting number will
     * begin.
     * @return True, go to next state.
     */
    private fun getFirstTurn(): Boolean {
        // get both player starting numbers
        val player1Num =
                players[PlayerId.PLAYER1.id].startNumber?.number ?: return false
        val player2Num =
                players[PlayerId.PLAYER2.id].startNumber?.number ?: return false

        // player with higher number starts
        if (player2Num > player1Num) {
            // player 2 starts
            // will be incremented again in run()
            state = GameState.TURN_PLAYER_1
            turnCounter.firstTurn = GameState.TURN_PLAYER_2
            return true
        }
        return true
    }

    /**
     * Init state. Exchange random generated starting numbers.
     * @param playerId Id of player
     */
    private fun init(playerId: PlayerId): Boolean {
        val id = playerId.id

        // exchange starting number between players
        // local player will generate random number
        // proxy player will receive number via communication handler
        return players[id].startNumber?.run {
            val otherId = (id + 1) % players.size
            players[otherId].processOthersStartingNumber(this)

            true
        } ?: false
    }

    /**
     * Make players turn.
     * @param playerId Id of active player
     * @return True if the move is completed.
     */
    private fun makePlayersTurn(playerId: PlayerId): Boolean {
        val id = playerId.id

        // allow player to make move, pieces on the grid are touchable
        players[id].allow = true

        // get players move and put into the other player
        val otherId = (id + 1) % players.size
        players[id].move?.also {
            players[otherId].othersMove = it
            moveHistory[playerId]!!.add(it)
        } ?: return false
        players[id].move = null

        // players move is over, pieces cannot be moved
        players[id].allow = false

        // present move to gui, run in engine
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
    fun win(player: Player) {
        Gdx.app.log(Constants.TAG_GAME, "player ${player.id} won the game!")
        state = GameState.GAME_OVER

        if (players[1] is PlayerProxy) {
            // unregister from event bus
            StrategoGame.unregister(players[1])
            // close connection
            if (CommunicationHandler.iCom.isConnected) {
                CommunicationHandler.iCom.disconnect()
            }
        }

        // switch to end game screen
        if (player.id == PlayerId.PLAYER1)
            StrategoGame.switchScreen(Screens.GAME_WON)
        else
            StrategoGame.switchScreen(Screens.GAME_LOST)
    }


    /**
     *  Surrenders the current game.
     */
    fun surrender() {
        CommunicationHandler.serialize(ControlMessage(ControlEvent.SURRENDER))
        win(players[1])
    }

    /**
     * [CommunicationHandler] event occurs the connection was lost.
     * Show connection lost dialog and returns to [MainMenuScreen].
     * @param event OnErrorEvent
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onErrorEvent(event: OnErrorEvent) {
        // show dialog and return to main
        if (state != GameState.GAME_OVER)
            DialogProvider.showConnectionLost(HudController.stage)
    }

    /**
     * [CommunicationHandler] event if data was received.
     * @param event Event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun OnDataReceivedEvent(event: DataReceivedEvent) {
        when (val obj = CommunicationHandler.deserialize(event.data)) {
            is ControlMessage -> processControlEvent(obj.event)
        }
    }

    /**
     * Processes control events.
     * @param event Event
     */
    private fun processControlEvent(event: ControlEvent) {
        when (event) {
            ControlEvent.SURRENDER -> win(players[0])
        }
    }
}