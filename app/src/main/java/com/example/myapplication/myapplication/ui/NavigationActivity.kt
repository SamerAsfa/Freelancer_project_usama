package com.example.myapplication.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.myapplication.HiltApplication
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.ui.fragments.HistoryFragment
import com.example.myapplication.myapplication.ui.fragments.HomeFragment
import com.example.myapplication.myapplication.ui.fragments.MoreFragment
import com.example.myapplication.myapplication.ui.fragments.NotificationsFragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_navigation.*


class NavigationActivity : AppCompatActivity() {


    fun clearAndStart(context: Context) {
        val intent = Intent(context, NavigationActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        HiltApplication.mContext = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
//        logOutButton.setOnClickListener {
//            LongTermManager.getInstance().clearAll()
//            SplashActivity().clearAndStart(this)
//        }


        val homeFragment = HomeFragment.getInstance()
        val historyFragment = HistoryFragment.getInstance()
        val notificationsFragment = NotificationsFragment()
        val moreFragment = MoreFragment()


        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.history -> setCurrentFragment(historyFragment)
                R.id.notification -> setCurrentFragment(notificationsFragment)
                R.id.more -> setCurrentFragment(moreFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }



}