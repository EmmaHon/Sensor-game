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

    private val pingPongUrl = "https://en.wikipedia.org/wiki/Table_tennis"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.info_screen, container, false)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickListeners()
    }
    private fun setOnClickListeners() {
        clickPp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(pingPongUrl)
            startActivity(intent)
        }
        Toast.makeText(activity,"Tap to Ping Pong history", Toast.LENGTH_SHORT).show()
    }
}
