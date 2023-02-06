package com.hsn.pianotiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hsn.pianotiles.databinding.ActivityMainBinding
import com.hsn.pianotiles.gameview.TileBoardView
import com.hsn.pianotiles.handler.ClassicTilesBoardHandler
import com.hsn.pianotiles.handler.ITile
import com.hsn.pianotiles.handler.ITileCallback

class MainActivity : AppCompatActivity(), ITileCallback {

    private lateinit var binding: ActivityMainBinding

    private lateinit var tilesBoardHandler: ITile

    private var isResume = false

    private lateinit var tileBoardView: TileBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        tileBoardView = binding.container
        tilesBoardHandler = ClassicTilesBoardHandler(this)
        tileBoardView.setTilesBoardHandler(tilesBoardHandler)


    }

    override fun onResume() {
        super.onResume()
        isResume = true
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun setGameBegin() {

    }

    override fun setGameOver() {
        Thread.sleep(1000)
        tilesBoardHandler.reset()
    }

    override fun updateScore(score: Float) {
        runOnUiThread {
            binding.score.text = String.format("%.3f", score)
        }
    }
}