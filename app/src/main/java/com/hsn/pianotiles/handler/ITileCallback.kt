/**
 * Created by Hussain on 2/6/2023
 *
 */

package com.hsn.pianotiles.handler

interface ITileCallback {
    fun setGameBegin()
    fun setGameOver()
    fun updateScore(score : Float)
}