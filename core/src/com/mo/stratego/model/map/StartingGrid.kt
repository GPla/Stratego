package com.mo.stratego.model.map

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Rank
import com.mo.stratego.model.player.Player

/**
 * A Grid representing the [Player]s starting formation.
 * It has the size of an 4 x 10 array.
 */
class StartingGrid {

    private val matrix = Array(4) { arrayOfNulls<Rank?>(10) }
    val sizeX = matrix[0].size
    val sizeY = matrix.size


    /**
     * Overload of [] operator. Sets the value at point.x, point.y.
     * @param point Point
     * @param value Value
     */
    operator fun set(point: GridPoint2, value: Rank?) =
            set(point.x, point.y, value)

    /**
     * Overload of [] operator. Sets the value at x,y.
     * @param x X
     * @param y Y
     * @param value Value
     */
    operator fun set(x: Int, y: Int, value: Rank?) {
        checkBounds(x, y)
        matrix[x][y] = value
    }

    /**
     * Overload of [] operator.
     * @param point Point
     * @return The [Rank] at position x,y.
     */
    operator fun get(point: GridPoint2): Rank? = get(point.x, point.y)

    /**
     * Overload of [] operator.
     * @param x X
     * @param y Y
     * @return The [Rank] at position x,y.
     */
    operator fun get(x: Int, y: Int): Rank? {
        checkBounds(x, y)
        return matrix[x][y]
    }


    /**
     * Check if index is in bound of array dimensions.
     * @param x X
     * @param y Y
     */
    private fun checkBounds(x: Int, y: Int) {
        if (x >= matrix[0].size)
            throw Exception("Index out of bound: x = $x")
        if (y >= matrix.size)
            throw Exception("Index out of bound: y = $y")
    }

    /**
     * @return Returns if all values are set.
     */
    fun isValid() = matrix.all { it.all { r -> r != null } }
}