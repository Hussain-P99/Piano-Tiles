package com.hsn.pianotiles.utils

object Util {

    fun map(number: Int, original: IntRange, target: IntRange): Int {
        val ratio = number.toFloat() / (original.endInclusive - original.start)
        return (ratio * (target.endInclusive - target.start)).toInt()
    }

}