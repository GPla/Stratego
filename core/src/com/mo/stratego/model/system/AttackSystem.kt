package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.mo.stratego.model.Result
import com.mo.stratego.model.component.AttackComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.WaitComponent
import com.mo.stratego.model.game.GameController
import com.mo.stratego.util.Constants

/**
 * A system that processes entities with a [PieceComponent] and a
 * [AttackComponent].
 */
class AttackSystem :
    IteratingSystem(Family.all(PieceComponent::class.java,
                               AttackComponent::class.java)
                            .exclude(WaitComponent::class.java).get()) {

    // component mapper
    private val pieceMapper = ComponentMapper.getFor(PieceComponent::class.java)
    private val attackMapper =
            ComponentMapper.getFor(AttackComponent::class.java)

    /**
     * Performs the attack, depending on the [Result], actions are taken.
     */
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null)
            return

        val piece = pieceMapper.get(entity)?.piece ?: return
        val enemy = attackMapper.get(entity)?.piece ?: return

        val result = piece.rank.attacks(enemy.rank)

        Gdx.app.log(Constants.TAG_GAME, "result: $result")

        // process attack
        when (result) {
            Result.GAME_WON -> GameController.win(piece.owner)
            Result.WON      -> {
                // attacking piece won
                // move enemy piece to graveyard
                piece.showDefault()
                enemy.returnToDefaultPosition()
            }
            Result.LOST     -> {
                // attacking piece lost
                // move to graveyard
                enemy.showDefault()
                piece.returnToDefaultPosition()
            }
            Result.DRAW     -> {
                // both pieces are dead
                // move both to graveyard
                enemy.returnToDefaultPosition()
                piece.returnToDefaultPosition()
            }
        }

        entity.remove(AttackComponent::class.java)
    }
}