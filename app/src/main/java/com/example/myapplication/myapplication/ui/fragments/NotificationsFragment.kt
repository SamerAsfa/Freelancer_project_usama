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
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.adapters.NotificationAdapter
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
        return view
    }


    fun getHistoryApi() {
        try {
            val call = apiInterface?.getNotificationHistoryApi()
            call?.enqueue(object : retrofit2.Callback<ArrayList<NotificationModel>?> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<NotificationModel>?>,
                    response: retrofit2.Response<ArrayList<NotificationModel>?>
                ) {
                    val arrayHistory = response.body()
                    if (arrayHistory?.size!! > 0) {
                        val scanMainAdapter = NotificationAdapter(arrayHistory)
                        mainView?.notificationRecyclerView?.setHasFixedSize(true)
                        mainView?.notificationRecyclerView?.layoutManager =
                            LinearLayoutManager(requireContext())
                        mainView?.notificationRecyclerView?.adapter = scanMainAdapter
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<ArrayList<NotificationModel>?>,
                    t: Throwable
                ) {
                    call.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }


}