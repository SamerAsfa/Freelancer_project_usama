package com.example.myapplication.myapplication.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseFragment
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.common.NotificationType
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.data.APIInterface
import com.example.myapplication.myapplication.domain.model.GetAllNotificationsResponse
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.NotificationModel.Companion.toLeaveModel
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.EditRequstLeaveActivity
import com.example.myapplication.myapplication.ui.NotificationInfoActivity
import com.example.myapplication.myapplication.ui.adapters.MyTeamNotificationAdapter
import com.example.myapplication.myapplication.ui.adapters.NotificationAdapter
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import java.util.*

class NotificationsFragment : BaseFragment() {
    protected var mainView: View? = null
    var userModel: UserModel? = null
    var apiInterface: APIInterface? = null

    var myNotificationAdapter: NotificationAdapter = NotificationAdapter()
    var myTeamNotificationAdapter: MyTeamNotificationAdapter =
        MyTeamNotificationAdapter()

    companion object {
        val fragmentName: String = "NotificationsFragment"
        val notificationsFragment: NotificationsFragment? = null
        fun getInstance(): NotificationsFragment {
            return notificationsFragment ?: NotificationsFragment()
        }
    }

    override fun onResume() {
        super.onResume()
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
        adapterListener()
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
                        myNotificationAdapter.submitData(arrayHistory.notifications as ArrayList<NotificationModel>)
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

                        myTeamNotificationAdapter.submitData(arrayHistory.notifications as ArrayList<NotificationModel>)
                        mainView?.myTeamNotificationRecyclerView?.setHasFixedSize(true)
                        mainView?.myTeamNotificationRecyclerView?.layoutManager =
                            LinearLayoutManager(requireContext())
                        mainView?.myTeamNotificationRecyclerView?.adapter =
                            myTeamNotificationAdapter
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

    private fun initUIListener(view: View) {

        view.backArrowImageButton.setOnClickListener {

        }

        view.clearAllTextView.setOnClickListener {
            val userId: String = userModel?.id.toString()
            deleteAllNotification(userId)
        }

        view.myNotificationTextView.setOnClickListener {
            view.myNotificationTextView.setTextColor(Color.parseColor("#000000"))
            view.notificationUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            view.myTeamNotificationTextView.setTextColor(Color.parseColor("#708792"))
            view.teamNotificationUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            view.notificationRecyclerView.visibility = View.VISIBLE
            view.myTeamNotificationRecyclerView.visibility = View.GONE
            view.clearAllTextView.visibility = View.VISIBLE
        }


        view.myTeamNotificationTextView.setOnClickListener {
            view.myTeamNotificationTextView.setTextColor(Color.parseColor("#000000"))
            view.teamNotificationUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            view.myNotificationTextView.setTextColor(Color.parseColor("#708792"))
            view.notificationUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            view.notificationRecyclerView.visibility = View.GONE
            view.myTeamNotificationRecyclerView.visibility = View.VISIBLE
            view.clearAllTextView.visibility = View.GONE
        }

    }

    private fun deleteAllNotification(id: String) {
        try {
            val call = apiInterface?.deleteAllNotification(id)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>?,
                    response: retrofit2.Response<Any?>?
                ) {
                  //  val responseBody = response?.body()
                    if (response?.isSuccessful == true) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully Deleted notifications",
                            Toast.LENGTH_LONG
                        ).show()
                        myNotificationAdapter.clearData()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Not Deleted Pleas try again",
                            Toast.LENGTH_LONG
                        ).show()
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

    private fun handelUIVisibility(view: View) {

        if (!userModel?.role.toString().contains("teamLead", ignoreCase = true)) {
            view.roleConstraintLayout.visibility = View.GONE
        }

    }

    private fun fillUIData() {
        //   unreadMyNotificationTextView.text = unReadNotification.toString()
    }

    private fun adapterListener() {

        myNotificationAdapter.clearNotificationOnItemClick = {model ->
            model?.id?.let { deleteNotification(it) }
        }

        myNotificationAdapter.editLeaveRequestOnItemClick = {model ->
            val intent = Intent(requireActivity() , EditRequstLeaveActivity::class.java)
            intent.putExtra("Leave_Request_model", model?.toLeaveModel())
            startActivity(intent)
        }
        myNotificationAdapter.deleteLeaveRequestOnItemClick = { model ->
            model?.let { showCancelLeaveRequestDialog(it) }
        }

        myNotificationAdapter.onItemClick ={model->
            val intent =Intent(requireContext() ,NotificationInfoActivity::class.java)
            intent.putExtra("notification_model", model)
            intent.putExtra("notification_type", NotificationType.MyNotification)
            startActivity(intent)
        }


        myTeamNotificationAdapter.approvedOnItemClick = { model ->
            model?.item_id?.let { approveLeave(it.toInt()) }
        }

        myTeamNotificationAdapter.rejectOnItemClick = { model ->
            model?.item_id?.let { rejectLeave(it.toInt()) }
        }

        myTeamNotificationAdapter.deleteNotificationSettingsOnItemClick = { model ->
            model?.id?.let { deleteNotification(it) }
        }

        myTeamNotificationAdapter.onItemClick ={model->
            val intent =Intent(requireContext() ,NotificationInfoActivity::class.java)
            intent.putExtra("notification_model", model)
            intent.putExtra("notification_type", NotificationType.MyTeamNotification)
            startActivity(intent)
        }
    }

    private fun showCancelLeaveRequestDialog(model: NotificationModel) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_leave_request_dilaog)

        val yesBtn = dialog.findViewById(R.id.yesButtonDeleteLeaveRequestDialog) as Button
        val cencelBtn = dialog.findViewById(R.id.cancelButtonDeleteLeaveRequestDialog) as Button
        yesBtn.setOnClickListener {
            model.item_id?.let { it1 -> cancelLeaveRequest(it1.toInt()) }
            dialog.dismiss()
        }
        cencelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun cancelLeaveRequest(leaveRequestId: Int) {
        try {
            val call = apiInterface?.cancelLeaveRequest(leaveRequestId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully Canceled ",
                            Toast.LENGTH_LONG
                        ).show()
                        // refresh data
                        getMyNotificationApi()
                        getMyTeamNotificationApi()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        requireContext(),
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                requireContext(),
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun deleteNotification(notificationId: String) {
        try {
            val call = apiInterface?.deleteNotification(notificationId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully Deleted",
                            Toast.LENGTH_LONG
                        ).show()
                        // refresh data
                        getMyNotificationApi()
                        getMyTeamNotificationApi()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        requireContext(),
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                requireContext(),
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun approveLeave(leaveId: Int) {
        try {
            val call = apiInterface?.approveLeave(leaveId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully Approved",
                            Toast.LENGTH_LONG
                        ).show()

                        getMyTeamNotificationApi()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        requireContext(),
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                requireContext(),
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun rejectLeave(leaveId: Int) {
        try {
            val call = apiInterface?.rejectLeave(leaveId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully Rejected",
                            Toast.LENGTH_LONG
                        ).show()
                        // refresh data
                       getMyTeamNotificationApi()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        requireContext(),
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                requireContext(),
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}