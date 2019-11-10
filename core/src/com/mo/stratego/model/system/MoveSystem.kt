package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.mo.stratego.model.Piece
import com.mo.stratego.model.component.AttackComponent
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent
import com.mo.stratego.model.map.Grid

/**
 * System that processes entities with a [PieceComponent], a [PositionComponent]
 * and a [MoveComponent]. This system moves the entities to their new position,
 * if an opponent's [Piece] occupies the target position an attack is initiated.
 */
class MoveSystem : IteratingSystem(
        Family.all(PieceComponent::class.java, PositionComponent::class.java,
                   MoveComponent::class.java)
                .exclude(AttackComponent::class.java).get()) {

    // component mapper
    private val posMapper =
            ComponentMapper.getFor(PositionComponent::class.java)
    private val movMapper = ComponentMapper.getFor(MoveComponent::class.java)
    private val pieceMapper = ComponentMapper.getFor(PieceComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val position = posMapper.get(entity)?.position
        val move = movMapper.get(entity)?.move
        val piece = pieceMapper.get(entity)?.piece

        if (entity == null ||
            position == null ||
            move == null ||
            piece == null)
            return

        val newPoint = position.cpy().add(move)

        //FIXME: beautify
        // check if move is valid
        // the removal of the movecomponent triggers an update of the gamegrid
        when (Grid.isCellAllowed(Grid.translatePositionToCell(newPoint),
                                 piece.owner)) {
            0 -> piece.remove(MoveComponent::class.java)
            1 -> {
                position.add(move)
                piece.remove(MoveComponent::class.java)
            }
            2 -> {
                val enemy = Grid[newPoint]
                if (enemy != null)
                    piece.add(AttackComponent(enemy))
            }
        }
    }
}