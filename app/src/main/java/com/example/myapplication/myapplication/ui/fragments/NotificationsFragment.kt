package com.example.myapplication.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.error.VolleyError
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseFragment
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.MyLeavesActivity
import com.example.myapplication.myapplication.ui.SplashActivity
import com.example.myapplication.myapplication.ui.adapters.HistoryAdapter
import com.example.myapplication.myapplication.ui.adapters.LeavesAdapter
import com.example.myapplication.myapplication.ui.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.activity_my_leaves.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_more.view.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import java.util.ArrayList
import java.util.HashMap

class NotificationsFragment : BaseFragment() {
    protected var mainView: View? = null
    var userModel: UserModel? = null

    companion object {
        val fragmentName: String = "NotificationsFragment"
        val notificationsFragment: NotificationsFragment? = null
        fun getInstance(): NotificationsFragment {
            return notificationsFragment ?: NotificationsFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = LongTermManager.getInstance().userModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_notifications, container, false)
        mainView = view
       getHistoryApi()
        return view
    }


    fun getHistoryApi() {
        POSTMediasTask().get(
            requireActivity(),
            BaseRequest.notificationsApi,
            "",
            object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    val arrayHistory: ArrayList<NotificationModel?>? = response?.let {
                        NotificationModel().parseArray(
                            it
                        )
                    }
                    if(arrayHistory?.size!! >0) {
                        val scanMainAdapter = NotificationAdapter(arrayHistory)
                        mainView?.notificationRecyclerView?.setHasFixedSize(true)
                        mainView?.notificationRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
                        mainView?.notificationRecyclerView?.adapter = scanMainAdapter
                    }
                }

                override fun onErrorCall(error: VolleyError?) {
                    showDialogOneButtonsCustom("Error", error?.message.toString(), "Ok") { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        )
    }



}