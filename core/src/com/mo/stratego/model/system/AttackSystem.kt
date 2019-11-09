package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.mo.stratego.model.GameController
import com.mo.stratego.model.Result
import com.mo.stratego.model.component.AttackComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.PositionComponent

/**
 * System that evaluates battles.
 */
class AttackSystem :
    IteratingSystem(Family.all(PieceComponent::class.java,
                               AttackComponent::class.java).get()) {

    // component mapper
    val pieceMapper = ComponentMapper.getFor(PieceComponent::class.java)
    val attackMapper = ComponentMapper.getFor(AttackComponent::class.java)

    /**
     * Processes the attack.
     */
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null)
            return

        val piece = pieceMapper.get(entity)?.piece ?: return
        val enemy = attackMapper.get(entity)?.piece ?: return

        val result = piece.rank.attacks(enemy.rank)

        Gdx.app.log("attack", "result: $result")

        when (result) {
            Result.GAME_WON -> GameController.win(piece.owner)
            Result.WON      -> removeEntity(enemy)
            Result.LOST     -> removeEntity(piece)
            Result.DRAW     -> {
                removeEntity(enemy)
                removeEntity(piece)
            }
        }

        entity.remove(AttackComponent::class.java)
    }

    /**
     * Removes the [Entity] from the game and grid.
     * The [PositionComponent] is removed, so that the Grid
     * can be correctly updated.
     */
    fun removeEntity(entity: Entity) {
        entity.remove(PositionComponent::class.java)
        engine.removeEntity(entity)
    }
}