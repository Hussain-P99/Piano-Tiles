/**
 * Created by Hussain on 2/5/2023
 *
 */

package com.hsn.pianotiles.thread

import android.graphics.Canvas
import android.view.SurfaceHolder
import com.hsn.pianotiles.handler.ITile

class GameThread(
    private val surfaceHolder: SurfaceHolder
) : Thread() {

    val fps = 60

    val frameRate = 1000L / fps

    private var isRunning = false


    private var handler: ITile? = null

    fun setRunning(isRunning: Boolean) {
        this.isRunning = isRunning
    }

    fun setHandler(handler : ITile) {
        this.handler = handler
    }

    override fun run() {
        var canvas: Canvas? = null


        while (isRunning) {
            try {
                if (handler == null) continue

                canvas = surfaceHolder.lockCanvas() ?: continue

                handler!!.draw(canvas)
                if (!handler!!.isFinished()) {
                    handler!!.next()
                }

                surfaceHolder.unlockCanvasAndPost(canvas)

            } catch (e: Exception) {
                if (surfaceHolder.surface.isValid) {
                    surfaceHolder.unlockCanvasAndPost(canvas ?: continue)
                }
            }


        }
    }
}