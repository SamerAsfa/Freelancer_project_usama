package com.example.myapplication.myapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.myapplication.R

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    companion object {
        val fragmentName : String = "NotificationsFragment"
        val notificationsFragment: NotificationsFragment? = null
        fun getInstance(): NotificationsFragment {
            return notificationsFragment ?: NotificationsFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("")
    }


}