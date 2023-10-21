package com.rmxburhan.game2d

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.webkit.RenderProcessGoneDetail
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity() : AppCompatActivity() {
    private lateinit var gameScreen : Game
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_main)
        gameScreen = findViewById(R.id.gameScreen)
        val btnPlay = findViewById<Button>(R.id.btn)

        gameScreen.onFinished = {
            btnPlay.visibility = VISIBLE
        }

     btnPlay.setOnClickListener {
         gameScreen.play()
         btnPlay.visibility = GONE
        }

    }
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision
            }
        }

}