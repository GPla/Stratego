package com.mo.stratego.model.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Rank
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.player.Player
import com.mo.stratego.model.player.PlayerId

/**
 * This class represents the game grid. The [EntityListener] is
 * triggered if a [Piece] has a [PositionComponent] and
 * independently, another time, if it has a [MoveComponent].
 */
object Grid : EntityListener {

    /**
     * Playing grid (10x10), with the origin in the top left corner.
     * The y-axis is flipped compared to the camera, which has the origin
     * in the bottom left corner.
     */
    private val matrix: Array<Array<Piece?>> =
            Array(10) { arrayOfNulls<Piece?>(10) }

    /**
     * A map that maps the rank to the number of pieces in their default
     * position, off game grid, for both [Player]s.
     */
    val spawnMap: Map<Rank, Array<Int>> = mapOf(
            Rank.BOMB to arrayOf(0, 0),
            Rank.MARSHAL to arrayOf(0, 0),
            Rank.GENERAL to arrayOf(0, 0),
            Rank.COLONEL to arrayOf(0, 0),
            Rank.MAJOR to arrayOf(0, 0),
            Rank.CAPTAIN to arrayOf(0, 0),
            Rank.LIEUTENANT to arrayOf(0, 0),
            Rank.SERGEANT to arrayOf(0, 0),
            Rank.MINER to arrayOf(0, 0),
            Rank.SCOUT to arrayOf(0, 0),
            Rank.SPY to arrayOf(0, 0),
            Rank.BOMB to arrayOf(0, 0),
            Rank.FLAG to arrayOf(0, 0))

    private val posMapper: ComponentMapper<PositionComponent> =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val moveMapper: ComponentMapper<MoveComponent> =
            ComponentMapper.getFor(MoveComponent::class.java)

    /**
     * Updates the grid if a [MoveComponent] or [PositionComponent] from
     * a [Piece] is removed.
     * @param entity Entity
     */
    override fun entityRemoved(entity: Entity?) {
        // safety check
        if (entity !is Piece)
            return

        update(entity, 1)
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

        update(entity, 0)
    }

    /**
     * Reloads the complete grid.
     */
    //TODO: implement
    fun reload() {

    }

    /**
     * Updates the position of the [Piece] on the grid.
     * @param piece Piece
     * @param type 0 - added, 1 - removed
     */
    private fun update(piece: Piece, type: Int) {
        val position = posMapper.get(piece)?.position
        removePiece(piece)

        position?.also {
            // add piece to board
            val point = translatePositionToCell(position)

            // update new position
            if (inBound(point)) {
                matrix[point.x][point.y] = piece
            }

            // FIXME moving a piece falsely trigger decrement
            // update spawn counter on added-entity event
            if (type == 0) {
                var sign = if (position == piece.rank.getDefaultPosition(
                                piece.owner.id)) 1 else -1
                spawnMap.getValue(piece.rank)[piece.owner.id.id] += sign
            }
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
     * Calculates the [GameMap] position; the origin is in the bottom
     * left corner.
     * @param cell Cell
     * @return the [GameMap] position of the piece.
     */
    fun translateCellToPosition(cell: GridPoint2): GridPoint2 {
        return GridPoint2(cell.x + GameMap.gridLeft,
                          cell.y + GameMap.gridBottom)
    }

    /**
     * @param position Position
     * @return Whether or not the position is in bound of the game map.
     */
    private fun inBound(position: GridPoint2) =
            position.x in (0..9) && position.y in (0..9)

    /**
     * @return  A string representation of the grid, with the origin in
     * bottom left corner. A '#' indicates an empty cell,
     * 'o' the lake and otherwise the [Player]'s id.
     */
    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        for (y in 9 downTo 0) {
            for (x in 0..9) {
                if ((x in 2..3 || x in 6..7) && y in 4..5)
                    builder.append("o ")
                else
                    builder.append("${matrix[x][y]?.owner?.id?.id ?: '#'} ")
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
     * Overload of the bracket operator. Expects matrix coordinates.
     * Call: Grid[x, y]
     * @param x X
     * @param y Y
     * @return [Piece] in the grid cell. Returns null if cell is empty or
     * indices are out of bound.
     */
    operator fun get(x: Int, y: Int): Piece? {
        // out of bound
        if (x < 0 || x >= matrix.size ||
            y < 0 || y >= matrix.size)
            return null

        return matrix[x][y]
    }

    /**
     * Checks for possible moves for a [Piece] in horizontal
     * and vertical direction.
     * @param piece Piece
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

    /**
     * Collects the free cells in the [Player]'s placement zone.
     * @param playerId Id of the [Player]
     * @return A List of free cells in the [Player]'s zone.
     */
    fun getFreeCellsInPlayerZone(playerId: PlayerId): List<GridPoint2> {
        val cells = mutableListOf<GridPoint2>()

        // iterate over players zone
        for (y in getPlayersZone(playerId)) {
            for (x in 0..9) {
                // check if cell is empty
                if (matrix[x][y] == null)
                    cells.add(translateCellToPosition(GridPoint2(x, y)))
            }
        }

        return cells
    }

    /**
     * @param playerId Id of the [Player]
     * @return The y-axis range of the [Player]'s placement zone.
     */
    private fun getPlayersZone(playerId: PlayerId): IntRange {
        return when (playerId) {
            PlayerId.PLAYER1 -> 0..3
            PlayerId.PLAYER2 -> 6..9 // nice
        }
    }
}