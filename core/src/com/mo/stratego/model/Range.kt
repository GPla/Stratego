package com.mo.stratego.model

/**
 * Enum of ranges.
 * @param range number of field horizontally or vertically
 */
enum class Range(val range: Int) {
    IMMOBILE(0),    // cannot move
    NORMAL(1),      // can move 1 tile
    ANY(9);         // can move any


    companion object {
        /**
         * @param rank Rank
         * @return The [Range] for the [Rank].
         */
        fun getRange(rank: Rank): Range {
            return when (rank) {
                Rank.FLAG, Rank.BOMB -> IMMOBILE
                Rank.SCOUT           -> ANY
                else                 -> NORMAL
            }
        }
    }
}
