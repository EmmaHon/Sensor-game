package com.example.anni.riggedpongsensorproject

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //check if the sensor is available
        addStartMenuFragment()
    }

    private fun addStartMenuFragment() {
        val mStartMenuFragment = StartMenuFragment()
        fragmentManager.beginTransaction().add(R.id.start_menu_fragment, mStartMenuFragment).commit()
    }
}
