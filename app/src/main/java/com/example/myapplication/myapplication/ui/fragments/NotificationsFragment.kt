package com.example.myapplication.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseFragment
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.data.APIInterface
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.domain.model.GetAllNotificationsResponse
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import java.util.*

class NotificationsFragment : BaseFragment() {
    protected var mainView: View? = null
    var userModel: UserModel? = null
    var apiInterface: APIInterface? = null

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
        apiInterface = APIClient.client?.create(APIInterface::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_notifications, container, false)
        mainView = view
        getHistoryApi()
        initUIListener()
        return view
    }


    fun getHistoryApi() {
        try {
            val call = apiInterface?.getNotificationHistoryApi()
            call?.enqueue(object : retrofit2.Callback<GetAllNotificationsResponse?> {
                override fun onResponse(
                    call: retrofit2.Call<GetAllNotificationsResponse?>?,
                    response: retrofit2.Response<GetAllNotificationsResponse?>?
                ) {
                    val arrayHistory = response?.body()
                    if (arrayHistory?.notifications?.size!! > 0) {
                        val scanMainAdapter = NotificationAdapter(arrayHistory.notifications as ArrayList<NotificationModel>, requireContext())
                        mainView?.notificationRecyclerView?.setHasFixedSize(true)
                        mainView?.notificationRecyclerView?.layoutManager =
                            LinearLayoutManager(requireContext())
                        mainView?.notificationRecyclerView?.adapter = scanMainAdapter
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<GetAllNotificationsResponse?>?,
                    t: Throwable
                ) {
                    call?.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun initUIListener(){

    /*    clearAllTextView.setOnClickListener {
            val userId:String = userModel?.id.toString()
            deleteAllNotification(userId)
        }*/
    }

    private fun deleteAllNotification(id:String){
        try {
            val call = apiInterface?.deleteAllNotification(id)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>?,
                    response: retrofit2.Response<Any?>?
                ) {
                    val responseBody = response?.body()

                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>?,
                    t: Throwable
                ) {
                    call?.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }

}