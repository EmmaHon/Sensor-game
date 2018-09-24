package com.example.anni.riggedpongsensorproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val libGDXLauncherBtn : Button = findViewById<Button>(R.id.libGDXLauncherBtn)

        libGDXLauncherBtn.setOnClickListener {
            launchlibGDX()
        }
    }

    private fun launchlibGDX() {
        val intent = Intent(this, Launcher::class.java)
        startActivity(intent)
    }
}
