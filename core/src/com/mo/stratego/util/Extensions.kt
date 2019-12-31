package com.mo.stratego.util

/**
 * Extension method to format [Float]s.
 * Float without decimal places.
 * @param digits number of digits
 */
fun Float.formatNoDecimal(digits: Int) = "%${digits}.0f".format(this)

/**
 * Extension method to format [Float]s.
 * Float without decimal places.
 */
fun Float.formatNoDecimal() = "%.0f".format(this)
