package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.HighlightType
import com.mo.stratego.model.MoveType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.*
import com.mo.stratego.model.map.Grid

/**
 * System that processes entities with a [PieceComponent], a [PositionComponent]
 * and a [MoveComponent]. This system moves the entities to their new position,
 * if an opponent's [Piece] occupies the target position an attack is initiated.
 */
class MoveSystem : IteratingSystem(
        Family.all(PieceComponent::class.java, PositionComponent::class.java,
                   MoveComponent::class.java)
                .exclude(AttackComponent::class.java,
                         WaitComponent::class.java).get()) {

    // component mapper
    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val movMapper = ComponentMapper.getFor(MoveComponent::class.java)
    private val pieceMapper = ComponentMapper.getFor(PieceComponent::class.java)

    /**
     * Process the movement.
     * @param entity Entity
     * @param deltaTime Float
     */
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null)
            return

        val position = posMapper.get(entity)?.position ?: return
        val move = movMapper.get(entity) ?: return
        val piece = pieceMapper.get(entity)?.piece ?: return

        when (move.type) {
            MoveType.RELATIVE -> moveRelative(piece, position, move.move)
            MoveType.ABSOLUTE -> moveAbsolute(piece, move.move)
        }
    }

    /**
     * Moves the [Piece] relative to the current [PositionComponent].
     * The move is validated with the [Grid] class.
     *
     * @param piece Piece to move
     * @param position Current position
     * @param move Move
     */
    private fun moveRelative(piece: Piece, position: GridPoint2,
                             move: GridPoint2) {
        val newPoint = position.cpy().add(move)

        // check if move is valid
        // the removal of the move component triggers an update of the game grid
        when (Grid.isCellAllowed(Grid.translatePositionToCell(newPoint),
                                 piece.owner)) {
            0 -> piece.remove(MoveComponent::class.java) // invalid move
            1 -> {
                highlightMovement(piece, position, move)

                //move to new position
                position.add(move)
                piece.remove(MoveComponent::class.java)
            }
            2 -> {
                highlightMovement(piece, position, move)

                val enemy = Grid[newPoint]
                enemy?.run {
                    // show front side texture
                    showFront()
                    piece.showFront()
                    // perform attack
                    piece.add(WaitComponent(0.4f))
                    piece.add(AttackComponent(this))
                }
            }
        }

    }

    /**
     * Moves the [Piece] to the position. No validation is made.
     * @param piece Piece to move
     * @param move New position
     */
    private fun moveAbsolute(piece: Piece, move: GridPoint2) {
        // An entity can only have one instance of each component
        // adding one, that is already existing, overwrites it.
        piece.add(PositionComponent(move))
        piece.remove(MoveComponent::class.java)
    }

    /**
     * Highlights the old and new position.
     * @param piece Piece
     * @param position old position
     * @param move move
     */
    private fun highlightMovement(piece: Piece, position: GridPoint2,
                                  move: GridPoint2) {
        HighlightType.deleteHighlight(engine, HighlightType.MOVE)

        arrayOf(Entity(), Entity()).forEachIndexed { index, entity ->
            HighlightType.createHighlight(entity, piece, HighlightType.MOVE,
                                          null)
            when (index) {
                0 -> entity.add(PositionComponent(position.cpy(), -1))
                1 -> entity.add(PositionComponent(position.cpy().add(move), -1))
            }
            engine.addEntity(entity)
        }
    }
}