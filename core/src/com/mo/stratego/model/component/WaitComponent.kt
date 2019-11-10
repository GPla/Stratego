package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component

//TODO desc
class WaitComponent(val waitTime: Float) : Component {
    var time: Float = 0f
}