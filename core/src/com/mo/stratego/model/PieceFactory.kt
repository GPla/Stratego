package com.mo.stratego.model

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.player.Player

/**
 * Factory for generating [Piece]s
 */
object PieceFactory {
    /**
     * Generates a full set of [Piece]s for a given [Player].
     * @param owner Player
     * @return A list containing the appropriate number of [Piece]s per [Rank].
     */
    fun generateSet(owner: Player): List<Piece> {
        val pieces = mutableListOf<Piece>()

        for (rank in Rank.values()) {
            pieces.addAll(generatePieces(rank, owner, rank.count))
        }

        return pieces
    }

    /**
     * Generates the given amount of [Piece]s of the same [Rank].
     * @param rank Rank
     * @param owner Owner
     * @param count Amount to generate
     * @return A list of the generated [Piece]s.
     */
    fun generatePieces(rank: Rank, owner: Player, count: Int): List<Piece> {
        val list = mutableListOf<Piece>()
        for (it in 1.rangeTo(count)) {
            list.add(Piece(rank, owner))
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