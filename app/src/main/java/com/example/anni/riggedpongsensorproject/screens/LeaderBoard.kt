package com.example.anni.riggedpongsensorproject.screens


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.anni.riggedpongsensorproject.MainActivity
import com.example.anni.riggedpongsensorproject.R



class LeaderBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard_screen)
        //TODO: check if the sensor is available
        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        val backtoMenuBtn = findViewById<Button>(R.id.startMenu)
        backtoMenuBtn.setOnClickListener {
            launchStartMenu()
        }
    }
    private fun launchStartMenu() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}