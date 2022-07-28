package com.example.myapplication.myapplication.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.myapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_auth)

        initNavGraph()
    }


    private fun initNavGraph(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.user_auth_nav_graph) as NavHostFragment
        val navController = navHostFragment.navController
    }
}