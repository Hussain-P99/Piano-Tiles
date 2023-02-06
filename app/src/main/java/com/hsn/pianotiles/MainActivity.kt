package com.hsn.pianotiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.hsn.pianotiles.databinding.ActivityMainBinding
import com.hsn.pianotiles.gameview.TileBoardView
import com.hsn.pianotiles.handler.ClassicTilesBoardHandler
import com.hsn.pianotiles.handler.GameType
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
        tilesBoardHandler = ClassicTilesBoardHandler(this,GameType.RELAY)
        tileBoardView.setTilesBoardHandler(tilesBoardHandler)

        binding.reset.setOnClickListener {
            if (tilesBoardHandler.isGameOver()) {
                tilesBoardHandler.reset()
            }
        }

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
    }

    override fun updateScore(score: String) {
        runOnUiThread {
            binding.score.text = score
        }
    }
}