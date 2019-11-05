package com.mo.stratego.util

class Scale {
    companion object {
        // scale: 32 pixels equals 1 game unit
        val unitscale: Float = 1 / 64f

        fun getPixelToUnit(pixel: Float) = pixel * unitscale
    }
}