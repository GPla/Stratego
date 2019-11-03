package com.mo.stratego.model

/**
 * Enum of all directions a playing piece can direct
 */
enum class Direction(val col: Int) {
    DOWN(0),
    LEFT(1),
    RIGHT(2),
    UP(3)
}