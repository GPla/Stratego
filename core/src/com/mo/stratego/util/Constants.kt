package com.mo.stratego.util

object Constants {
    // scale: 32 pixels equals 1 game unit
    val UNITSCALE: Float = 1 / 64f

    fun getPixelToUnit(pixel: Float) = pixel * UNITSCALE
}