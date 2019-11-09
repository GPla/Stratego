package com.mo.stratego.model

/**
 * Enum with of all piece ranks
 * @property rank short title
 * @property r_int internal use
 * @property title long title
 */
enum class Rank(val rank: String,
                private val r_int: Int,
                val title: String) {

    BOMB("B", 11, "Bomb"),
    MARSHAL("10", 10, "Marshal"),
    GENERAL("9", 9, "General"),
    COLONEL("8", 8, "Colonel"),
    MAJOR("7", 7, "Major"),
    CAPTAIN("6", 6, "Captain"),
    LIEUTENANT("5", 5, "Lieutenant"),
    SERGEANT("4", 4, "Sergeant"),
    MINER("3", 3, "Miner"),
    SCOUT("2", 2, "Scout"),
    SPY("1", 1, "Spy"),
    FLAG("F", 0, "Flag");

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

}