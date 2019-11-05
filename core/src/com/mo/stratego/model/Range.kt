package com.mo.stratego.model

/**
 * Enum of movement ranges
 */
enum class Range(val range : Int) {
    IMMOBILE(0),    // cannot move
    NORMAL(1),      // can move 1 field
    STRAIGHT(9)     // can move any number of fields in a straight line
}