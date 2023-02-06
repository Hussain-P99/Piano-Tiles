/**
 * Created by Hussain on 2/4/2023
 *
 */

package com.hsn.pianotiles.handler

import android.graphics.Canvas
import com.hsn.pianotiles.core.Tile
import com.hsn.pianotiles.utils.ViewPortHandler

interface ITile {
    fun next()
    fun reset()
    fun initializeTiles()
    fun draw(canvas: Canvas)
    fun onClick(x: Float, y: Float)
    fun getViewPortHandler() : ViewPortHandler
    fun setViewPortHandler(viewPortHandler: ViewPortHandler)
    fun getTiles(): ArrayList<ArrayList<Tile>>
    fun getRows(): Int
    fun getCols(): Int
    fun isBegin(): Boolean
    fun setGameOver()
    fun isGameOver(): Boolean
    fun checkTilesClicked(x: Int, y: Int)
    fun checkWhiteTileClicked(x: Int, y: Int): Boolean
    fun getScore() : Float
    fun isFinished(): Boolean
}