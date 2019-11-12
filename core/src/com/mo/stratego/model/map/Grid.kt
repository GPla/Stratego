package com.mo.stratego.model.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.player.Player

/**
 * This class represents the game grid. The [EntityListener] is
 * triggered if a [Piece] has a [PositionComponent] and
 * independently, another time, if it has a [MoveComponent].
 */
//TODO: piece defeated
object Grid : EntityListener {

    // playing grid, origin is top left, camera/map has bottom left
    private val matrix: Array<Array<Piece?>> =
            Array(10) { arrayOfNulls<Piece?>(10) }

    // component mapper for faster access
    private val posMapper: ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val moveMapper: ComponentMapper<MoveComponent> =
            ComponentMapper.getFor(MoveComponent::class.java)

    /**
     * Updates the grid if a [MoveComponent] from a [Piece] is removed.
     * @param entity Entity
     */
    override fun entityRemoved(entity: Entity?) {
        Gdx.app.log("dtag1", "${entity == null}")
        // safety check
        if (entity !is Piece)
            return

        update(entity)
    }

    /**
     * Updates the grid if a [Piece] with [PositionComponent] and
     * no [MoveComponent] was added.
     * @param entity Entity
     */
    override fun entityAdded(entity: Entity?) {
        // safety check
        if (entity !is Piece)
            return

        // ignore movement for now
        val move = moveMapper.get(entity)
        if (move != null)
            return

        update(entity)
    }

    /**
     * Reloads the complete grid.
     */
    //TODO: implement
    fun reload() {

    }

    /**
     * Updates the grid.
     * @param piece Piece
     */
    private fun update(piece: Piece) {
        val position = posMapper.get(piece)?.position
        if (position != null) { // entered family
            // add piece to board
            val point = translatePositionToCell(position)
            // remove old position
            removePiece(piece)
            matrix[point.x][point.y] = piece
        } else { // removed from board
            removePiece(piece)
        }
    }

    /**
     * Removes [Piece] from the grid.
     * @param piece Piece
     */
    private fun removePiece(piece: Piece) {
        for (y in 0..9) {
            for (x in 0..9) {
                if (matrix[x][y] == piece) {
                    matrix[x][y] = null
                    break
                }
            }
        }
    }

    /**
     * Calculates the grid position; the origin is in the bottom left corner,
     * same as camera.
     * @param position Position
     * @return the grid position of the piece.
     */
    fun translatePositionToCell(position: GridPoint2): GridPoint2 {
        return GridPoint2(position.x - GameMap.gridLeft,
                          position.y - GameMap.gridBottom)
    }


    /**
     * @return  A string representation of the grid. A '#' indicates an
     * empty cell, 'o' the lake and otherwise the [Player]'s id.
     */
    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        for (y in 9 downTo 0) {
            for (x in 0..9) {
                if ((x in 2..3 || x in 6..7) && y in 4..5)
                    builder.append("o ")
                else
                    builder.append("${matrix[x][y]?.owner?.id ?: '#'} ")
            }
            builder.appendln()
        }
        return builder.toString()
    }

    /**
     * Overload of the bracket operator. Expects map coordinates.
     * Call: Grid[Gridpoint2(x, y)]
     * @param position GridPoint2
     * @return [Piece] in the grid cell. Returns null if cell is empty or
     * indices are out of bound.
     */
    operator fun get(position: GridPoint2): Piece? {
        val point = translatePositionToCell(position)

        // out of bound
        if (point.x < 0 || point.x >= matrix.size ||
            point.y < 0 || point.y >= matrix.size)
            return null

        return matrix[point.x][point.y]
    }

    /**
     * Checks for possible moves for a [Piece] in horizontal
     * and vertical direction.
     * @param standpoint Standpoint of the [Piece]
     * @param range Range of the [Piece]
     * @param owner Owner of the [Piece]
     * @return A list of allowed moves in horizontal and vertical direction.
     */
    fun getAllowedMoves(piece: Piece): List<GridPoint2> {

        // check if piece is on board
        val position = posMapper.get(piece)?.position ?: return emptyList()
        val standpoint = translatePositionToCell(position)

        val allowedMoves: MutableList<GridPoint2> = mutableListOf()
        var blocked = BooleanArray(4) // blocked direction

        for (r in 1..piece.range.range) {
            // possible moves in all directions
            val moves =
                    listOf(GridPoint2(r, 0), GridPoint2(-r, 0),
                           GridPoint2(0, r), GridPoint2(0, -r))

            // check which moves are valid
            moves.forEachIndexed { index, move ->
                if (!blocked[index]) {
                    when (isCellAllowed(standpoint.cpy().add(move),
                                        piece.owner)) {
                        0 -> blocked[index] = true
                        1 -> allowedMoves.add(move)
                        2 -> {
                            blocked[index] = true
                            allowedMoves.add(move)
                        }
                    }
                }
            }

            // all directions blocked
            if (blocked.all { b -> b })
                break

        }
        return allowedMoves
    }

    /**
     * @param point Grid position of the [Piece]
     * @param owner owner of the [Piece]
     * @return Whether or not the cell can be occupied by a [Piece].
     * A 2 indicates that the cell is occupied by an opponent's [Piece].
     */
    fun isCellAllowed(point: GridPoint2, owner: Player): Int {
        //out of bound
        if (point.x < 0 || point.x >= matrix.size ||
            point.y < 0 || point.y >= matrix.size)
            return 0

        // lakes
        if ((point.x in 2..3 || point.x in 6..7) && point.y in 4..5)
            return 0

        // check if cell is blocked by a piece of the same owner
        val owner2 = matrix[point.x][point.y]?.owner ?: return 1
        // the 2 indicates that the next move in this direction is blocked
        return if (owner != owner2) 2 else 0
    }
}