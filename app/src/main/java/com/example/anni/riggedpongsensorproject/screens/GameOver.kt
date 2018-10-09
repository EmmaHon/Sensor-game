package com.example.anni.riggedpongsensorproject.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.anni.riggedpongsensorproject.Launcher
import com.example.anni.riggedpongsensorproject.R
import kotlinx.android.synthetic.main.gameover_screen.*


class GameOver : AppCompatActivity () {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameover_screen)
        //TODO: check if the sensor is available
        setOnClickListeners()




        //Score
        val score = getIntent().getIntExtra("SCORE", 0)
        tv_scoreLabel.setText("" + score)

        //Save High Score
        val settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE)
        var highScore = settings.getInt("HIGH_SCORE", 0)
        var highScore2 = settings.getInt("HIGH_SCORE2", 0)
        var highScore3 = settings.getInt("HIGH_SCORE3", 0)

        //Update highscores
        if (score > highScore){
            tv_highScoreLabel.setText("" + score)
            //Save score to high score
            val editor = settings.edit()
            editor.putInt("HIGH_SCORE", highScore)
            editor.commit()
        }
        if(score > highScore2){
            tv_highScoreLabel.setText("" + score)
            var temp = highScore2
            highScore2 = score
            highScore = temp
            //Save score to high score
            val editor = settings.edit()
            editor.putInt("HIGH_SCORE", highScore)
            editor.putInt("HIGH_SCORE2", highScore2)
            editor.commit()
        }
        if(score > highScore3){
            tv_highScoreLabel.setText("" + score)
            val temp = highScore3
            highScore3 = score
            highScore2 = temp
            //Save score to high score
            val editor = settings.edit()
            editor.putInt("HIGH_SCORE2", highScore2)
            editor.putInt("HIGH_SCORE3", highScore3)
            editor.commit()
        }else{
            tv_highScoreLabel.setText("" + highScore)
        }

    }

    private fun setOnClickListeners() {
        tryA.setOnClickListener {
            tryAgain()
        }
        leaderboard_button.setOnClickListener {
            launchLeaderboard()
        }
    }

    private fun tryAgain() {
        val intent = Intent(applicationContext, Launcher::class.java)
        startActivity(intent)
    }

    private fun launchLeaderboard() {
        val LeaderBoardintent = Intent(applicationContext, LeaderBoard::class.java)
        startActivity(LeaderBoardintent)
    }
}

