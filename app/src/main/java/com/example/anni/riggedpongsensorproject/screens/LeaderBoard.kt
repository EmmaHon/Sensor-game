package com.example.anni.riggedpongsensorproject.screens

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.example.anni.riggedpongsensorproject.MainActivity
import com.example.anni.riggedpongsensorproject.R
import kotlinx.android.synthetic.main.leaderboard_screen.*



class LeaderBoard : AppCompatActivity() {

    private lateinit var mainlistview: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard_screen)
        //TODO: check if the sensor is available
        setOnClickListeners()

        var scoreList = ArrayList<Int>()
        scoreList.add(0)
        scoreList.add(1)
        scoreList.add(2)
        scoreList.add(3)


    }


    private fun setOnClickListeners() {
        startMenu.setOnClickListener {
            launchStartMenu()
        }
    }

    private fun launchStartMenu() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}




