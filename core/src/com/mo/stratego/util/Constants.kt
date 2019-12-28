package com.mo.stratego.util

/**
 * Class that contains game constants.
 */
object Constants {
    // scale: 64 pixels equals 1 game unit
    const val TILESCALE: Float = 64f
    const val UNITSCALE: Float = 1 / TILESCALE

    /**
     *  Calculates the world unit to a given pixel value.
     * @param pixel Float
     * @return The pixel value as game unit.
     */
    fun getPixelToUnit(pixel: Float) = pixel * UNITSCALE

    /**
     * Calculates the pixel value to a given world unit.
     * @param unit Float
     * @return The world unit as pixel value.
     */
    fun getUnitToPixel(unit: Float) = unit * TILESCALE


    //LOGGING
    const val TAG_BLUETOOTH = "bluetooth"
    const val TAG_MAP = "map"
    const val TAG_GAME = "game"
    const val TAG_ERROR = "error"

}