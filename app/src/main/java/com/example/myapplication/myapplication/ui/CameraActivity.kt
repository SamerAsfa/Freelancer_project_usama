package com.example.myapplication.myapplication.ui

import android.content.pm.ActivityInfo
import android.graphics.Camera
import android.media.MediaRecorder
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import java.io.File
import java.io.IOException


class CameraActivity : AppCompatActivity()   {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_surface)


    }

}