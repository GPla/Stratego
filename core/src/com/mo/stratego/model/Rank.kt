package com.mo.stratego.model

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.player.PlayerId

/**
 * Enum with of all piece ranks
 * @property rank short title
 * @property count number of pieces with this rank
 * @property r_int internal use
 * @property title long title
 */
enum class Rank(val rank: String,
                val count: Int,
                private val r_int: Int,
                val title: String) {

    BOMB("B", 6, 11, "Bomb"),
    MARSHAL("10", 1, 10, "Marshal"),
    GENERAL("9", 1, 9, "General"),
    COLONEL("8", 2, 8, "Colonel"),
    MAJOR("7", 3, 7, "Major"),
    CAPTAIN("6", 4, 6, "Captain"),
    LIEUTENANT("5", 4, 5, "Lieutenant"),
    SERGEANT("4", 4, 4, "Sergeant"),
    MINER("3", 5, 3, "Miner"),
    SCOUT("2", 8, 2, "Scout"),
    SPY("1", 1, 1, "Spy"),
    FLAG("F", 1, 0, "Flag");

    /**
     * @param rank opponent's rank
     * @return [Result] of this attacking rank.
     */
    fun attacks(rank: Rank): Result {
        // illegal moves
        if (this == BOMB || this == FLAG)
            return Result.ILLEGAL

        // draw, equal rank
        if (this == rank)
            return Result.DRAW

        // won or lost
        return when (rank) {
            FLAG    -> Result.GAME_WON
            BOMB    -> if (this == MINER) Result.WON else Result.LOST
            MARSHAL -> if (this == SPY) Result.WON else Result.LOST
            else    -> if (this.r_int > rank.r_int) Result.WON else Result.LOST
        }

    }


    fun getDefaultPosition(ownerId: PlayerId): GridPoint2 {
        val yOffset = ownerId.id * (GameMap.height - 4)
        return when (this) {
            BOMB -> GridPoint2(0, 2 + ownerId.id * -2 + yOffset)
            FLAG -> GridPoint2(1, 2 + ownerId.id * -2 + yOffset)
            else -> GridPoint2(10 - r_int, 1 + yOffset)

        }
    }
}