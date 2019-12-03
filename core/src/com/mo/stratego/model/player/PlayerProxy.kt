package com.mo.stratego.model.player

import com.badlogic.gdx.Gdx
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

//TODO desc
//TODO exchange othersmove
class PlayerProxy(id: PlayerId) : Player(id) {
    init {
        StrategoGame.register(this)
    }

    /**
     * Send move via [CommunicationHandler].
     */
    // FIXME: move executed on the wrong side
    override var othersMove: Move? = null
        set(value) {
            value?.also {
                // TODO put somewhere else, prettify
                val posGrid = Grid.translatePositionToCell(it.position)
                val pos = GridPoint2(9 - posGrid.x, 9 - posGrid.y)
                val mov = GridPoint2(it.move.x * -1, it.move.y * -1)

                CommunicationHandler.serialize(
                        Move(Grid.translateCellToPosition(pos), mov))
            }
            field = value
        }

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

    //TODO desc 
    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onDataReceived(event: DataReceivedEvent) {
        val data = event.data?.toString(Charsets.UTF_8) ?: return
        Gdx.app.log("bth", "proxy rec: $data")

        val obj = CommunicationHandler.deserialize(data)
        when (obj) {
            is StartNumber  -> startNumber = obj
            is Move         -> move = obj
            is StartingGrid -> startingGrid = obj
        }

    }

}