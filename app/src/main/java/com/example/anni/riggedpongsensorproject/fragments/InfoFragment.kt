package com.example.anni.riggedpongsensorproject.fragments

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.anni.riggedpongsensorproject.R
import kotlinx.android.synthetic.main.info_screen.*

class InfoFragment: Fragment() {

    val pingUrl = "https://www.athleticscholarships.net/history-of-table-tennis.htm"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.info_screen, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(activity, "Tap to Ping Pong history", Toast.LENGTH_SHORT).show()

        //link to Wikipedia
        clickPp.setOnClickListener {
            val Pintent = Intent(Intent.ACTION_VIEW)
            Pintent.data = Uri.parse(pingUrl)
            startActivity(Pintent)
        }
    }
}
