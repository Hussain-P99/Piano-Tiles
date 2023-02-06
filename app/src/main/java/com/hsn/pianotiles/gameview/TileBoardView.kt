/**
 * Created by Hussain on 2/4/2023
 *
 */

package com.hsn.pianotiles.gameview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.hsn.pianotiles.handler.ITile
import com.hsn.pianotiles.thread.GameThread

class TileBoardView @JvmOverloads constructor(
    context: Context, attrSet: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrSet, defStyleAttr), SurfaceHolder.Callback {

    private var tilesBoardHandler: ITile? = null

    private var gameThread: GameThread? = null

    init {
        holder.addCallback(this)
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setupTileBoardHandler(tilesBoardHandler ?: return)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        gameThread = GameThread(holder)
        gameThread?.setRunning(true)
        gameThread?.start()
        gameThread?.setHandler(tilesBoardHandler ?: return)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        if (gameThread?.isAlive == true) {
            gameThread?.setRunning(false)
            gameThread?.interrupt()
        }
    }

    private fun setupTileBoardHandler(tilesBoardHandler: ITile) {
        val viewPortHandler = tilesBoardHandler.getViewPortHandler()
        viewPortHandler.width = if (width % 2 == 1) width - 1 else width
        viewPortHandler.height = if (height % 2 == 1) height - 1 else height
        tilesBoardHandler.setViewPortHandler(viewPortHandler)
        tilesBoardHandler.initializeTiles()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)

        val action = event.actionMasked
        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                tilesBoardHandler?.onClick(event.x, event.y)
                true
            }
            MotionEvent.ACTION_UP -> {
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    fun getTilesBoardHandler(): ITile? {
        return tilesBoardHandler
    }

    fun setTilesBoardHandler(tilesBoardHandler: ITile) {
        this.tilesBoardHandler = tilesBoardHandler
        setupTileBoardHandler(this.tilesBoardHandler ?: return)
        gameThread?.setHandler(tilesBoardHandler)
    }


}

