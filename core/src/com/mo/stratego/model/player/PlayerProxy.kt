package com.mo.stratego.model.player

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.StrategoGame
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
class PlayerProxy(id: PlayerId) : Player(id) {
    init {
        StrategoGame.register(this)
    }

    //TODO exchange othersmove
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

    //TODO impl
    override fun processOthersGrid(otherGrid: StartingGrid) {
        // TODO exchange via bluetooth
    }

    /**
     * Sends the starting number to the connected device.
     * @param number Other players starting number
     */
    override fun processOthersStartingNumber(number: StartNumber) {
        val json = Json(JsonConfiguration.Stable)
        val jsonData = json.stringify(StartNumber.serializer(), number) + '\n'
        Gdx.app.log("bth", "json send : $jsonData")
        CommunicationHandler.iCom.writeData(jsonData.toByteArray())
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onDataReceived(event: DataReceivedEvent) {
        val data = event.data?.toString(Charsets.UTF_8) ?: return
        Gdx.app.log("bth", "proxy rec: $data")


        startNumber = json.parse(StartNumber.serializer(), data)


    }

}