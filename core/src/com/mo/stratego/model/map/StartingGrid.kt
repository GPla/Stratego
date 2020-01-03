package com.mo.stratego.model.map

import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Rank
import com.mo.stratego.model.player.Player
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * A Grid representing the [Player]s starting formation.
 * It has the size of an 4 x 10 array.
 */
@Serializable
class StartingGrid {
    // for serialization
    private val className = StartingGrid::class.java.name
    var matrix = Array(4) { arrayOfNulls<Rank?>(10) }
        private set
    @Transient
    val sizeX = matrix[0].size
    @Transient
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
        matrix[y][x] = value
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
        return matrix[y][x]
    }

    /**
     * ForEachIndexed extension method. Iterates through the grid with
     * the parameters being x, y, [Rank]?.
     * @param action Function3<Int, Int, Rank?, Unit>
     */
    inline fun forEachIndexed(action: (Int, Int, Rank?) -> Unit) {
        for (y in 0.until(sizeY)) {
            for (x in 0.until(sizeX)) {
                action(x, y, matrix[y][x])
            }
        }
    }

    /**
     * ForEachIndexed extension method. The Grid can be fully initialized
     * with this method. Parameters are x, y with return value of [Rank].
     * @param action Function2<Int, Int, Rank>
     */
    inline fun forEachIndexedSet(action: (Int, Int) -> Rank) {
        for (y in 0.until(sizeY)) {
            for (x in 0.until(sizeX)) {
                matrix[y][x] = action(x, y)
            }
        }
    }

    /**
     * ForEachIndexed extension method. Iterates through the transposed matrix.
     * Parameters are x, y, [Rank]?.
     * @param action Function3<Int, Int, Rank?, Unit>
     */
    inline fun forEachIndexedTransposed(action: (Int, Int, Rank?) -> Unit) {
        for (y in 0.until(sizeY)) {
            for (x in 0.until(sizeX)) {
                action(x, y, matrix[sizeY - (y + 1)][sizeX - (x + 1)])
            }
        }
    }

    /**
     * Check if index is in bound of array dimensions.
     * @param x X
     * @param y Y
     */
    private fun checkBounds(x: Int, y: Int) {
        if (x >= sizeX)
            throw Exception("Index out of bound: x = $x")
        if (y >= sizeY)
            throw Exception("Index out of bound: y = $y")
    }

    /**
     * @return Returns if all values are set.
     */
    fun isValid() = matrix.all { it.all { r -> r != null } }


    /**
     * Transposes the grid.
     * @return This for chaining.
     */
    fun transpose(): StartingGrid {
        val temp = Array(4) { arrayOfNulls<Rank>(10) }
        forEachIndexed { x, y, rank ->
            temp[sizeY - (y + 1)][sizeX - (x + 1)] = rank
        }
        matrix = temp
        return this
    }

}