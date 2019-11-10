package com.mo.stratego.model.player

//TODO desc
class PlayerProxy(id: Int) : Player(id) {
    override val allowTouch: Boolean = false
}