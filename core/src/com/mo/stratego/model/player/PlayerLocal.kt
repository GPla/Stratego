package com.mo.stratego.model.player

//TODO desc
class PlayerLocal(id: Int) : Player(id) {
    override val allowTouch: Boolean = true
}