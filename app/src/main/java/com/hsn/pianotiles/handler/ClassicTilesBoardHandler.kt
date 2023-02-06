/**
 * Created by Hussain on 2/4/2023
 *
 */

package com.hsn.pianotiles.handler

import android.graphics.Canvas
import android.os.CountDownTimer
import com.hsn.pianotiles.core.Tile
import com.hsn.pianotiles.core.TileType
import com.hsn.pianotiles.utils.Constants
import com.hsn.pianotiles.utils.Util
import com.hsn.pianotiles.utils.ViewPortHandler
import java.util.Timer
import java.util.TimerTask
import kotlin.math.roundToInt
import kotlin.random.Random

class ClassicTilesBoardHandler(
    private val iTileCallback: ITileCallback,
    private val gameType: GameType
) : ITile {


    private var tiles = ArrayList<ArrayList<Tile>>()

    private val rows = 4
    private val cols = 4

    private var viewPortHandler: ViewPortHandler = ViewPortHandler(0, 0)

    private var isStart = true

    private var gameOver: Boolean = false

    private var score: Float = 0f

    private var timer = Timer()

    private val rowMax: Int
        get() {
            return when (gameType) {
                GameType.CLASSIC -> 50
                GameType.ARCADE -> Int.MAX_VALUE
                GameType.ZEN -> Int.MAX_VALUE
                GameType.RELAY -> Int.MAX_VALUE
                GameType.RUSH -> Int.MAX_VALUE
                GameType.COLOR -> 50
            }
        }

    private var rowCreated = rows

    private val gameOverTimer: CountDownTimer?
        get() {
            return when (gameType) {
                GameType.ZEN -> object : CountDownTimer(Constants.ZEN_TIME * 1000L, 150L) {
                    override fun onTick(time: Long) {
                        score = (time / 1000f)
                        iTileCallback.updateScore(getScore())
                        if (gameOver) {
                            cancel()
                        }
                    }

                    override fun onFinish() {
                        setGameOver()
                    }
                }
                GameType.RELAY -> object : CountDownTimer(Constants.RELAY_TIME * 1000L, 150L) {
                    override fun onTick(time: Long) {
                        score = (time / 1000f)
                        iTileCallback.updateScore(getScore())
                        if (gameOver) {
                            cancel()
                        }
                    }

                    override fun onFinish() {
                        setGameOver()
                    }
                }
                else -> null
            }
        }

    private val scoreUpdateTask: TimerTask
        get() = object : TimerTask() {
            override fun run() {
                score += 1
                iTileCallback.updateScore(getScore())
            }
        }

    override fun initializeTiles() {
        tiles.clear()
        for (i in 0 until rows) {
            val newTiles = ArrayList<Tile>()
            val r = Random.nextInt(0, rows)
            for (j in 0 until cols) {
                val type = if (r == j) TileType.BLACK else TileType.WHITE
                val tile = Tile(type, viewPortHandler, rows, cols)
                newTiles.add(tile)
                tile.setFirstTile(i == rows - 1 && type == TileType.BLACK)
            }
            tiles.add(newTiles)
        }
    }

    override fun setViewPortHandler(viewPortHandler: ViewPortHandler) {
        this.viewPortHandler = viewPortHandler
        initializeTiles()
    }

    override fun getTiles(): ArrayList<ArrayList<Tile>> {
        return tiles
    }

    override fun getRows(): Int {
        return rows
    }

    override fun getCols(): Int {
        return cols
    }

    override fun isBegin(): Boolean {
        return isStart
    }

    override fun checkTilesClicked(x: Int, y: Int) {
        if (gameOver) return
        val yMax = if (isBegin()) cols - 1 else cols - 2
        if (y < yMax) return
        if (tiles[y][x].tileType == TileType.BLACK) {
            tiles[y][x].clicked()
            if (isBegin()) {
                isStart = false
                iTileCallback.setGameBegin()
                beginScoreUpdateTask()
            }
        }
    }

    private fun beginScoreUpdateTask() {
        when (gameType) {
            GameType.ZEN, GameType.RELAY -> gameOverTimer?.start()
            else -> {
                timer.scheduleAtFixedRate(scoreUpdateTask, 0, 1)
            }
        }
    }

    override fun checkWhiteTileClicked(x: Int, y: Int): Boolean {
        if (gameOver) return false
        if (tiles[y][x].tileType == TileType.WHITE) {
            tiles[y][x].clicked()
            return true
        } else {
            return false
        }
    }

    override fun getScore(): String {
        return when (gameType) {
            GameType.ZEN, GameType.RELAY -> {
                score.toString()
            }
            else -> {
                String.format("%.3f", score / 1000)
            }
        }
    }

    override fun setGameOver() {
        gameOver = true
        iTileCallback.setGameOver()
        timer.cancel()
        scoreUpdateTask.cancel()
    }

    override fun isGameOver(): Boolean = gameOver

    private fun removeLastRowAddAtTop() {
        if (gameOver) return
        tiles.removeAt(rows - 1)
        val newTiles = ArrayList<Tile>()
        if (rowCreated < rowMax) {
            val r = Random.nextInt(0, 4)
            for (i in 0 until 4) {
                val type = if (i == r) TileType.BLACK else TileType.WHITE
                newTiles.add(Tile(type, viewPortHandler, rows, cols))
            }
            tiles.add(0, newTiles)
            rowCreated++
        } else {
            for (i in 0 until 4) {
                val type = TileType.EMPTY
                newTiles.add(Tile(type, viewPortHandler, rows, cols))
            }
            tiles.add(0, newTiles)
        }
    }

    override fun next() {
        if (!isGameOver()) {
            var twoTilesClicked = 0
            for (r in tiles.size - 2 until tiles.size) {
                for (tile in getTiles()[r]) {
                    if (tile.tileType == TileType.BLACK && tile.isClicked()) {
                        twoTilesClicked++
                    }
                }
            }

            if (twoTilesClicked == 2) {
                removeLastRowAddAtTop()
            }
        }
    }

    override fun reset() {
        isStart = true
        gameOver = false
        score = 0f
        rowCreated = 0
        scoreUpdateTask.cancel()
        timer.cancel()
        timer.purge()
        timer = Timer()
        initializeTiles()
        iTileCallback.updateScore(getScore())
    }

    override fun draw(canvas: Canvas) {
        val tiles = getTiles()

        val rows = tiles.size
        val cols = tiles[0].size

        var xpos = 0
        for (i in 0 until rows) {
            var ypos = 0
            for (j in 0 until cols) {
                tiles[i][j].drawTile(canvas, ypos, xpos)
                ypos += (viewPortHandler.width / cols)
            }
            xpos += (viewPortHandler.height / rows)
        }
    }

    override fun onClick(x: Float, y: Float) {
        val dx = Util.map(x.roundToInt(), IntRange(0, viewPortHandler.width - 1), IntRange(0, 4))
        val dy = Util.map(y.roundToInt(), IntRange(0, viewPortHandler.height - 1), IntRange(0, 4))
        if (isBegin()) {
            checkTilesClicked(dx, dy)
        } else {
            if (checkWhiteTileClicked(dx, dy)) {
                setGameOver()
            } else {
                checkTilesClicked(dx, dy)
            }
        }
    }

    override fun getViewPortHandler(): ViewPortHandler {
        return viewPortHandler
    }

    override fun isFinished(): Boolean {
        // check first n - 1 rows contains Empty Tiles
        var finished = true
        for (i in 0 until rows - 1) {
            for (j in 0 until cols) {
                if (tiles[i][j].tileType == TileType.WHITE || tiles[i][j].tileType == TileType.BLACK) {
                    finished = false
                }
            }
        }
        if (finished) {
            removeLastRowAddAtTop()
            setGameOver()
        }
        return finished
    }
}