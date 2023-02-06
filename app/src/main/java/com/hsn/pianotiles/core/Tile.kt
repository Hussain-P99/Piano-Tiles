/**
 * Created by Hussain on 2/4/2023
 *
 */

package com.hsn.pianotiles.core

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.hsn.pianotiles.utils.ViewPortHandler

class Tile(
    var tileType: TileType,
    private var viewPortHandler: ViewPortHandler,
    rows: Int,
    cols: Int
) {

    private var width: Int = 0
    private var height: Int = 0

    private var isClicked = false

    private var tilePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var isFirstTile = false

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4.0f
        color = Color.DKGRAY
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    init {
        this.width = viewPortHandler.width / rows
        this.height = viewPortHandler.height / cols
    }

    fun drawTile(canvas: Canvas, xpos: Int, ypos: Int) {
        val rect = Rect(xpos, ypos, width + xpos, height + ypos)

        if (tileType == TileType.EMPTY) {
            tilePaint.color = Color.WHITE
            canvas.drawRect(rect, tilePaint)
        } else {
            if (tileType == TileType.BLACK) {
                if (isClicked) {
                    tilePaint.color = Color.LTGRAY
                } else {
                    tilePaint.color = Color.BLACK
                }
            } else {
                if (isClicked) {
                    tilePaint.color = Color.RED
                } else {
                    tilePaint.color = Color.WHITE
                }
            }


            canvas.drawLine(
                xpos.toFloat(),
                ypos.toFloat(),
                width + xpos.toFloat(),
                ypos.toFloat(),
                strokePaint
            )
            canvas.drawLine(
                xpos.toFloat(),
                ypos.toFloat(),
                xpos.toFloat(),
                height + ypos.toFloat(),
                strokePaint
            )

            canvas.drawRect(rect, tilePaint)

            if (isFirstTile) {
                val text = "GO"
                val bounds = Rect()
                textPaint.getTextBounds(text, 0, text.length, rect)
                canvas.drawText(
                    "GO",
                    (xpos + (width / 2) - bounds.width()).toFloat(),
                    ((ypos + (height / 2) - bounds.height()).toFloat()),
                    textPaint
                )
            }
        }
    }


    fun clicked() {
        isClicked = true
    }

    fun isClicked(): Boolean {
        return isClicked
    }

    fun setFirstTile(b: Boolean) {
        isFirstTile = b
    }
}