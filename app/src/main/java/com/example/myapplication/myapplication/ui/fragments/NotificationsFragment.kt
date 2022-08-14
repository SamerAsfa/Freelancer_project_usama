package com.example.myapplication.myapplication.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseFragment
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.data.APIInterface
import com.example.myapplication.myapplication.domain.model.GetAllNotificationsResponse
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.adapters.MyTeamNotificationAdapter
import com.example.myapplication.myapplication.ui.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import java.util.*

class NotificationsFragment : BaseFragment() {
    protected var mainView: View? = null
    var userModel: UserModel? = null
    var apiInterface: APIInterface? = null

    lateinit var myNotificationAdapter :NotificationAdapter
    lateinit var myTeamNotificationAdapter :MyTeamNotificationAdapter

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

        fillUIData()
        handelUIVisibility(view)
        getMyNotificationApi()
        getMyTeamNotificationApi()
        initUIListener(view)
        return view
    }


    fun getMyNotificationApi() {
        try {
            val call = apiInterface?.getNotificationHistoryApi()
            call?.enqueue(object : retrofit2.Callback<GetAllNotificationsResponse?> {
                override fun onResponse(
                    call: retrofit2.Call<GetAllNotificationsResponse?>?,
                    response: retrofit2.Response<GetAllNotificationsResponse?>?
                ) {
                    val arrayHistory = response?.body()
                    unreadMyNotificationTextView.text = response?.body()?.unread.toString()
                    if (arrayHistory?.notifications?.size!! > 0) {
                         myNotificationAdapter = NotificationAdapter(arrayHistory.notifications as ArrayList<NotificationModel>, requireContext())
                        mainView?.notificationRecyclerView?.setHasFixedSize(true)
                        mainView?.notificationRecyclerView?.layoutManager =
                            LinearLayoutManager(requireContext())
                        mainView?.notificationRecyclerView?.adapter = myNotificationAdapter
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

    fun getMyTeamNotificationApi() {
        try {
            val call = apiInterface?.getMyTeamNotifications()
            call?.enqueue(object : retrofit2.Callback<GetAllNotificationsResponse?> {
                override fun onResponse(
                    call: retrofit2.Call<GetAllNotificationsResponse?>?,
                    response: retrofit2.Response<GetAllNotificationsResponse?>?
                ) {
                    val arrayHistory = response?.body()
                    unreadMyTeamNotificationTextView.text = response?.body()?.unread.toString()
                    if (arrayHistory?.notifications?.size!! > 0) {
                         myTeamNotificationAdapter = MyTeamNotificationAdapter(arrayHistory.notifications as ArrayList<NotificationModel>, requireContext())
                        mainView?.myTeamNotificationRecyclerView?.setHasFixedSize(true)
                        mainView?.myTeamNotificationRecyclerView?.layoutManager =
                            LinearLayoutManager(requireContext())
                        mainView?.myTeamNotificationRecyclerView?.adapter = myTeamNotificationAdapter
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

    private fun initUIListener(view: View){

        view.clearAllTextView.setOnClickListener {
            val userId:String = userModel?.id.toString()
            deleteAllNotification(userId)
        }

        view.myNotificationTextView.setOnClickListener {
            view.myNotificationTextView.setTextColor(Color.parseColor("#000000"))
            view.notificationUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            view.myTeamNotificationTextView.setTextColor(Color.parseColor("#708792"))
            view.teamNotificationUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            view.notificationRecyclerView.visibility =View.VISIBLE
            view.myTeamNotificationRecyclerView.visibility =View.GONE
        }


        view.myTeamNotificationTextView.setOnClickListener {
            view.myTeamNotificationTextView.setTextColor(Color.parseColor("#000000"))
            view.teamNotificationUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            view.myNotificationTextView.setTextColor(Color.parseColor("#708792"))
            view.notificationUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            view.notificationRecyclerView.visibility =View.GONE
            view.myTeamNotificationRecyclerView.visibility =View.VISIBLE
        }

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
                    if(response?.isSuccessful == true){
                        Toast.makeText(requireContext() ,"Successfully Deleted notifications" ,Toast.LENGTH_LONG).show()
                        myNotificationAdapter.clearData()
                    }else{
                        Toast.makeText(requireContext() ,"Not Deleted Pleas try again" ,Toast.LENGTH_LONG).show()
                    }
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

    private fun handelUIVisibility(view: View){

        if(!userModel?.role.toString().contains("teamLead" , ignoreCase = true)) {
            view.roleConstraintLayout.visibility = View.GONE
        }

    }

    private fun fillUIData(){
     //   unreadMyNotificationTextView.text = unReadNotification.toString()
    }
}