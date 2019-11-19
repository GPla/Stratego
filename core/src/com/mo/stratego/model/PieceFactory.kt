package com.mo.stratego.model

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.player.Player

/**
 * Factory for generating [Piece]s
 */
object PieceFactory {
    /**
     * A map of [Rank] and the number per player.
     */
    private val countRanks: Map<Rank, Int> =
            mapOf(Rank.BOMB to 6,
                  Rank.MARSHAL to 1,
                  Rank.GENERAL to 1,
                  Rank.COLONEL to 2,
                  Rank.MAJOR to 3,
                  Rank.CAPTAIN to 4,
                  Rank.LIEUTENANT to 4,
                  Rank.SERGEANT to 4,
                  Rank.MINER to 5,
                  Rank.SCOUT to 8,
                  Rank.SPY to 1,
                  Rank.FLAG to 1)


    /**
     * Generates a full set of [Piece]s for a given [Player].
     * @param owner Player
     * @return A list containing the appropriate number of [Piece]s per [Rank].
     */
    fun generateSet(owner: Player): List<Piece> {
        if (owner.id !in 0..1)
            throw Exception("Unsupported Player Id: ${owner.id}")

        val initialPos = GridPoint2(0,
                                    if (owner.id == 0) 0
                                    else GameMap.height - 1)

        val pieces = mutableListOf<Piece>()

        for (entry in countRanks) {
            pieces.addAll(generatePieces(entry.key, owner,
                                         initialPos, entry.value))

        }

        return pieces
    }

    /**
     * Generates the given amount of [Piece]s of the same [Rank].
     * @param rank Rank
     * @param owner Owner
     * @param initialPos Initial position
     * @param count Amount to generate
     * @return A list of the generated [Piece]s.
     */
    fun generatePieces(rank: Rank, owner: Player, initialPos: GridPoint2,
                       count: Int): List<Piece> {
        if (owner.id !in 0..1)
            return emptyList()

        val list = mutableListOf<Piece>()
        for (it in 1.rangeTo(count)) {
            list.add(Piece(rank, owner, initialPos.cpy()))
            incrementPoint(initialPos, owner.id == 1)
        }
        return list
    }

    /**
     * Increments the x-axis value of a [GridPoint2]. If a row is full,
     * 10 pieces per row, the coordinates are set to the first column of the
     * next row.
     * @param point Point
     * @param yDown Orientation
     */
    fun incrementPoint(point: GridPoint2, yDown: Boolean = false) {
        if (point.x >= 9) {
            if (yDown)
                point.set(0, point.y - 1)
            else
                point.set(0, point.y + 1)
        } else
            point.add(1, 0)
    }
}