package com.mo.stratego.model.player

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.StrategoGame
import com.mo.stratego.model.Move
import com.mo.stratego.model.MoveType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.PieceFactory
import com.mo.stratego.model.communication.CommunicationHandler
import com.mo.stratego.model.communication.DataReceivedEvent
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.map.StartingGrid
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * This class is a proxy for a player that's connected via the
 * [CommunicationHandler]. The proxy will send actions made by the
 * local player and receives the actions made by the local player of the
 * connected device. The Architecture is symmetric and as follows
 * Local - Device 1 - Proxy - Network - Proxy - Device 2 - Local for a
 * 2 player game over the network.
 *
 * @param id Player id
 */
class PlayerProxy(id: PlayerId) : Player(id) {
    init {
        StrategoGame.register(this)
    }

    /**
     * Send move via [CommunicationHandler].
     */
    override var othersMove: Move? = null
        set(value) {
            value?.also {
                // send move in other players perspective
                CommunicationHandler.serialize(translateMove(it))
            }
            field = value
        }

    /**
     * Translate [Move] to the other [Player]'s perspective.
     * @param move Move
     * @return [Move] in the other [Player]'s perspective.
     */
    private fun translateMove(move: Move): Move {
        var posGrid = Grid.translatePositionToCell(move.position)
        val pos = GridPoint2(9 - posGrid.x, 9 - posGrid.y)
        posGrid = Grid.translateCellToPosition(pos)

        val mov = GridPoint2(move.move.x * -1, move.move.y * -1)
        return Move(posGrid, mov)
    }


    /**
     * Whether or not [Piece]'s of the player can be moved by the manually
     * on this device. Always false.
     */
    override var allow: Boolean = false
        get() = false

    private val json = Json(JsonConfiguration.Stable)

    override fun presentGrid(pieces: List<Piece>) {
        // group pieces by rank
        val rankPieces = pieces.toMutableList()
                .groupByTo(mutableMapOf()) { it.rank }

        val origin = GridPoint2(0, 6)

        // place pieces on grid
        startingGrid?.forEachIndexedTransposed { _, _, rank ->
            rankPieces[rank]?.let {
                val piece = it.removeAt(0)
                val move = Grid.translateCellToPosition(origin.cpy())
                piece.add(MoveComponent(move, MoveType.ABSOLUTE))
                PieceFactory.incrementPoint(origin)
            }
        }
    }

    /**
     * Send [StartingGrid] to via [CommunicationHandler].
     * @param otherGrid
     */
    override fun processOthersGrid(otherGrid: StartingGrid) {
        CommunicationHandler.serialize(otherGrid)
    }

    /**
     * Sends the starting number to the connected device.
     * @param number Other players starting number
     */
    override fun processOthersStartingNumber(number: StartNumber) {
        CommunicationHandler.serialize(number)
    }

    /**
     * Subscription of [DataReceivedEvent]'s that are broadcasted on the
     * EventBus. Event occurs if the [CommunicationHandler] receives
     * new data.
     * @param event DataReceivedEvent
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onDataReceived(event: DataReceivedEvent) {
        // deserialize json and assign to property
        when (val obj = CommunicationHandler.deserialize(event.data)) {
            is StartNumber -> startNumber = obj
            is Move -> move = obj
            is StartingGrid -> startingGrid = obj
        }
    }

}