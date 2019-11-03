package com.mo.stratego.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.mo.stratego.GameScreen
import com.mo.stratego.model.Piece

/**
 * Singleton class that controls the [Actor]s displayed on the [Stage]
 */
class FieldController(val screen : GameScreen,
                      val stage : Stage) : EntityListener {

    // Singleton
    companion object{
        var instance : FieldController? = null
        fun init(screen : GameScreen, stage : Stage) : FieldController {
            return when{
                instance != null -> instance!!
                else -> synchronized(this){
                    instance = FieldController(screen, stage)
                    return instance!!
                }
            }
        }

    }


    override fun entityAdded(entity: Entity?) {
        if(entity is Piece){
            stage.addActor(PieceActor(entity))
        }
    }

    override fun entityRemoved(entity: Entity?) {

    }


}