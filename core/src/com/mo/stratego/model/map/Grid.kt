package com.mo.stratego.model.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Range
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

    // playing grid with origin in the bottom left corner, same as camera
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
            matrix[point.x][point.y] = piece
        } else { // removed from family
            // remove piece from board
            for (y in 0..9) {
                for (x in 0..9) {
                    if (matrix[x][y] == piece) {
                        matrix[x][y] = null
                        break
                    }
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
    private fun translatePositionToCell(position: GridPoint2): GridPoint2 {
        return GridPoint2(position.x - GameMap.gridLeft,
                          position.y - GameMap.gridBottom)
    }


    /**
     * @return  A string representation of the grid. A zero indicates an
     * empty cell otherwise the [Player]'s id.
     */
    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        for (y in 0..9) {
            for (x in 0..9) {
                builder.append("${matrix[x][y]?.owner?.id ?: 0} ")
            }
            builder.appendln()
        }
        return builder.toString()
    }

    /**
     * Overload of the bracket operator. Expects the tilemap coordinates.
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
     * @param piece Piece
     * @return A list of allowed moves for the piece.
     */
    fun getAllowedMoves(piece: Piece): List<GridPoint2> {
        // check if piece is on board
        val position = posMapper.get(piece)?.position ?: return emptyList()

        val standpoint = translatePositionToCell(position)
        val moves = getPossibleMoves(piece.range)

        val allowedMoves = mutableListOf<GridPoint2>()

        for (move in moves) {
            val point = standpoint.cpy().add(move)
            if (isCellAllowed(point, piece.owner))
                allowedMoves.add(move)
        }
        return allowedMoves
    }

    /**
     * @param range Range
     * @return A list of possible moves in horizontal and vertical direction.
     */
    private fun getPossibleMoves(range: Range): List<GridPoint2> {
        val list: MutableList<GridPoint2> = mutableListOf()
        for (r in 1..range.range) {
            //FIXME: stay in bound
            //FIXME: go on after lake
            val directions =
                    listOf(GridPoint2(r, 0), GridPoint2(-r, 0),
                           GridPoint2(0, r), GridPoint2(0, -r))
            list.addAll(directions)
        }
        return list
    }

    /**
     * @param point Grid position of the [Piece]
     * @param owner owner of the [Piece]
     * @return Whether or not the cell can be occupied by a [Piece].
     */
    private fun isCellAllowed(point: GridPoint2, owner: Player): Boolean {
        // lakes
        if ((point.x in 2..3 || point.x in 6..7) && point.y in 4..5)
            return false

        // check if cell is blocked by a piece of the same owner
        //FIXME: owner check not working
        val owner2 = Grid[point]?.owner ?: return true
        return owner != owner2
    }


}