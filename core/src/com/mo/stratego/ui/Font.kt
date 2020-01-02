package com.mo.stratego.ui

/**
 * Enum of available fonts.
 *
 * @property size font size
 * @property internalName internal name used for generation
 */
enum class Font(val size: Int, val internalName: String) {
    SIZE26(26, "size26.ttf"),
    SIZE32(32, "size32.ttf"),
    SIZE48(48, "size48.ttf");
}