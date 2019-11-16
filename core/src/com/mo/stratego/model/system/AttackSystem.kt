package com.mo.stratego.model.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.GridPoint2
import com.mo.stratego.model.GameController
import com.mo.stratego.model.MoveType
import com.mo.stratego.model.Piece
import com.mo.stratego.model.Result
import com.mo.stratego.model.component.AttackComponent
import com.mo.stratego.model.component.MoveComponent
import com.mo.stratego.model.component.PieceComponent
import com.mo.stratego.model.component.WaitComponent
import com.mo.stratego.model.map.GameMap
import com.mo.stratego.model.map.Grid
import com.mo.stratego.model.player.Player

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

        Gdx.app.log("attack", "result: $result")

        when (result) {
            Result.GAME_WON -> GameController.win(piece.owner)
            Result.WON      -> removePiece(enemy)
            Result.LOST     -> removePiece(piece)
            Result.DRAW     -> {
                removePiece(enemy)
                removePiece(piece)
            }
        }

        // show default texture
        piece.showDefault()
        enemy.showDefault()

        entity.remove(AttackComponent::class.java)
    }

    /**
     * Removes the [Entity] from the [Grid] and places the [Piece]
     * in the graveyard of the [Player].
     */
    private fun removePiece(piece: Piece) {
        // move piece to graveyard
        // move component triggers and update of the grid
        piece.let {
            val cell = getGraveyardCell(it.owner)
            it.add(MoveComponent(GridPoint2(cell.x,
                                            if (it.owner.id == 0) cell.y
                                            else GameMap.height - cell.y - 1),
                                 MoveType.ABSOLUTE))

            // increment death counter
            it.owner.deathCounter++
        }
    }

    /**
     * @param owner Player
     * @return The next free cell in the graveyard of the [Player].
     */
    private fun getGraveyardCell(owner: Player): GridPoint2 {
        val column = owner.deathCounter % 10
        val row = owner.deathCounter / 10

        return GridPoint2(column, row)
    }
}