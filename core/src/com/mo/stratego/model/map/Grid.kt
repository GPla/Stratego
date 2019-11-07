package com.mo.stratego.model.map

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PositionComponent

/**
 * This class represents the game grid.
 */
object Grid : EntityListener {

    // playing grid with origin in the bottom left corner, same as camera
    private val matrix: Array<Array<Piece?>> = Array(10) { arrayOfNulls<Piece?>(10) }

    private val posMapper: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)
    private val moveMapper: ComponentMapper<MoveComponent> = ComponentMapper.getFor(MoveComponent::class.java)

    override fun entityRemoved(entity: Entity?) {
        //TODO: remove from grid
        //TODO: check for inconsistencies of movecomponent
    }

    /**
     * Update grid if a [Piece] with [PositionComponent] or
     * [MoveComponent] is added.
     */
    override fun entityAdded(entity: Entity?) {
        // check if playing piece
        if (entity !is Piece)
            return

        // check for movement
        val move = moveMapper.get(entity)
        if (move != null)
            update(move, entity)
        else {
            //check position
            val pos = posMapper.get(entity)
            if (pos != null)
                update(pos, entity)
        }
    }

    /**
     * Reloads the complete grid.
     */
    fun reload() {

    }

    /**
     * Updates grid with [MoveComponent]
     * @param move MoveComponent
     * @param piece Piece
     */
    private fun update(move: MoveComponent, piece: Piece) {
        // get piece position
        val position = posMapper.get(piece) ?: return
        val point = translatePositionToCell(position.position)
        matrix[point.x][point.y] = piece
    }

    /**
     * Updates grid with [PositionComponent]
     * @param position PositionComponent
     * @param piece Piece
     */
    private fun update(position: PositionComponent, piece: Piece) {
        val point = translatePositionToCell(position.position)
        matrix[point.x][point.y] = piece
    }

    /**
     * Calculates the grid position with the origin in
     * the bottom left corner, same as camera.
     * @param position Position
     * @return the grid position of the [Piece]
     */
    private fun translatePositionToCell(position: GridPoint2): GridPoint2 {
        return GridPoint2(position.x - GameMap.gridLeft,
                matrix.size - (position.y - GameMap.gridBottom) - 1)
    }


    /**
     * @return String of occupied cells.
     */
    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        for (y in 0..9) {
            for (x in 0..9) {
                builder.append("${if (matrix[x][y] != null) 1 else 0} ")
            }
            builder.appendln()
        }
        return builder.toString()
    }

    /**
     *  Overload of the bracket operator. Expects the tilemap coordinates.
     *  @param position GridPoint2
     * @return [Piece] at the grid cell. Returns null if cell is empty or
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
}