package com.example.myapplication.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.ui.MyLeavesActivity
import com.example.myapplication.myapplication.ui.SplashActivity
import com.example.myapplication.myapplication.ui.adapters.LeavesAdapter
import com.example.myapplication.myapplication.ui.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.activity_my_leaves.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_more.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import java.util.ArrayList

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    protected var mainView: View? = null

    companion object {
        val fragmentName: String = "NotificationsFragment"
        val notificationsFragment: NotificationsFragment? = null
        fun getInstance(): NotificationsFragment {
            return notificationsFragment ?: NotificationsFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_notifications, container, false)
        mainView = view
        val arrayHistory: ArrayList<NotificationModel?>? =
            LongTermManager.getInstance().notificationModelArrayList
        if(arrayHistory?.size!! >0) {
            val scanMainAdapter = NotificationAdapter(arrayHistory)
            view.notificationRecyclerView?.setHasFixedSize(true)
            view.notificationRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
            view.notificationRecyclerView?.adapter = scanMainAdapter
        }
        return view
    }




}