package com.mo.stratego.model

/**
 * Enum with of all piece ranks
 */
enum class Rank(val rank: String,
                private val rank_int: Int,
                val titel: String) {

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
     * Returns the [Result] of r1 attacking r2.
     * @return [Result] of r1 attacking r2.
     */
    fun beats(r1: Rank, r2: Rank): Result {
        // illegal moves
        if (r1 == BOMB || r1 == FLAG)
            return Result.ILLEGAL

        // draw, equal rank
        if (r1 == r2)
            return Result.DRAW

        // won or lost
        return when (r2) {
            FLAG -> Result.GAME_WON
            BOMB -> if (r1 == MINER) Result.WON else Result.LOST
            MARSHAL -> if (r1 == SPY) Result.WON else Result.LOST
            else -> if (r1.rank_int > r2.rank_int) Result.WON else Result.LOST
        }

    }

}