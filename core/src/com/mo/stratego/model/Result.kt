package com.mo.stratego.model

/**
 * Enum of all battle results
 */
enum class Result {

    GAME_WON,   // game won
    WON,        // battle won
    LOST,       // battle lost
    DRAW,       // draw, both pieces lose
    ILLEGAL     // illegal move

}