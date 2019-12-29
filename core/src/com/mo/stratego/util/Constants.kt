package com.mo.stratego.util

import com.badlogic.gdx.graphics.Color
import com.mo.stratego.model.map.GameMap

/**
 * Class that contains game constants.
 */
object Constants {
    // scale: 64 pixels equals 1 game unit
    const val TILESCALE: Float = 64f
    const val UNITSCALE: Float = 1 / TILESCALE

    // screen
    val SCREEN_WIDTH = getUnitToPixel(GameMap.width.toFloat())
    val SCREEN_HEIGHT = getUnitToPixel(GameMap.height.toFloat())

    // Logging Tags
    const val TAG_BLUETOOTH = "stratego_bluetooth"
    const val TAG_MAP = "stratego_map"
    const val TAG_GAME = "stratego_game"
    const val TAG_ERROR = "stratego_error"
    const val TAG_SOUND = "stratego_sound"

    // paths
    const val RULES_PATH = "rules/rules.md"

    // colors
    val YELLOW = Color(1f, 0.84375f, 0f, 1f)
    val BLACK = Color(0.1875f, 0.1875f, 0.1875f, 1f)

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


}