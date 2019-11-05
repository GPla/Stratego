package com.mo.stratego.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mo.stratego.GameScreen
import com.mo.stratego.model.Piece

/**
 * Singleton class that controls the [Actor]s displayed on the [Stage].
 * Implements [EntityListener] to add a [PieceActor] to the [Stage] if
 * a [Piece] is added to the [Engine].
 */
class FieldController(val screen : GameScreen,
                      val stage : Stage,
                      val engine : Engine) : EntityListener {


    // Singleton
    companion object{
        var instance : FieldController? = null
        fun init(screen : GameScreen, stage : Stage, engine : Engine) : FieldController {
            return when{
                instance != null -> instance!!
                else -> synchronized(this){
                    instance = FieldController(screen, stage, engine)
                    return instance!!
                }
            }
        }

    }

    override fun entityAdded(entity: Entity?) {
        // add if class = piece
        if(entity is Piece){
            stage.addActor(PieceActor(entity, engine))
        }
    }

    override fun entityRemoved(entity: Entity?) {

    }


}