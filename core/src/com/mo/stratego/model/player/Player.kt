package com.mo.stratego.model.player

//TODO desc
/**
 * Inspired by https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf
 * @property id Int
 * @property allowTouch Boolean
 * @constructor
 */
abstract class Player(val id: Int) {

    abstract val allowTouch: Boolean

    //TODO: implement https://pdfs.semanticscholar.org/f35c/df2b5cb1a36d703ab6c4a4d80cbaaf3cc603.pdf
}