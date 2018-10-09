package com.example.anni.riggedpongsensorproject.screens


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.anni.riggedpongsensorproject.Launcher
import com.example.anni.riggedpongsensorproject.R
import kotlinx.android.synthetic.main.gameover_screen.*


class GameOver: AppCompatActivity () {

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit game?")
        builder.setMessage("Do you want to exit game?")
        builder.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->
            finish()
        }
        )
        builder.setNegativeButton("No", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameover_screen)
        //TODO: check if the sensor is available
        setOnClickListeners()

        //var tvScore = findViewById<TextView>(R.id.tv_scoreLabel)
        //val tvHighScore = findViewById<TextView>(R.id.tv_highScoreLabel)

        val score = getIntent().getIntExtra("SCORE", 0)
        tv_scoreLabel.setText("" + score)

        val settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE)
        val highScore = settings.getInt("HIGH_SCORE", 0)

        if (score > highScore){
            tv_highScoreLabel.setText("" + score)

            //Save score to high score
            val editor = settings.edit()
            editor.putInt("HIGH_SCORE", score)
            editor.commit()
        }else{
            tv_highScoreLabel.setText("" + highScore)
        }

    }

    private fun setOnClickListeners() {
        val gameLauncherBtn = findViewById<android.widget.ImageButton>(R.id.tryA)
        val leaderboardBtn = findViewById<android.widget.ImageButton>(R.id.leaderboard_btn)
        gameLauncherBtn.setOnClickListener {
            tryAgain()
        }
        leaderboardBtn.setOnClickListener {
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

