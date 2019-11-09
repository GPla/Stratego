package com.mo.stratego.model.player

/**
 * Enum of player types
 */
enum class PlayerType {
    LOCAL,
    PROXY;

    companion object{
        /**
         * Factory method for [Player] types
         */
        fun createPlayer(type : PlayerType, id : Int) : Player{
            return when (type){
                LOCAL -> PlayerLocal(id)
                PROXY -> PlayerProxy(id)
            }
        }
    }
}