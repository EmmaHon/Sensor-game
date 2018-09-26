package com.example.anni.riggedpongsensorproject

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class StartMenuFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.start_menu_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        val gameLauncherBtn = view.findViewById<Button>(R.id.start_game_btn)
        val leaderboardBtn = view.findViewById<Button>(R.id.leaderboard_btn)
        gameLauncherBtn.setOnClickListener {
            launchGame()
        }
        leaderboardBtn.setOnClickListener {
            launchLeaderboardFragment()
        }
    }

    private fun launchGame() {
        val intent = Intent(activity, Launcher::class.java)
        startActivity(intent)
    }

    private fun launchLeaderboardFragment() {

    }
}