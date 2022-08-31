package com.example.myapplication.myapplication.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.common.NotificationType
import com.example.myapplication.myapplication.common.UserNoteType
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.data.APIInterface
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.models.NotificationModel.Companion.toLeaveModel
import kotlinx.android.synthetic.main.activity_notification_info.*
import java.text.SimpleDateFormat


class NotificationInfoActivity : AppCompatActivity() {

    lateinit var notificationModel: NotificationModel
    lateinit  var notificationType: NotificationType
    var apiInterface: APIInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_info)
        apiInterface = APIClient.client?.create(APIInterface::class.java)
        notificationModel = intent.extras?.getParcelable<NotificationModel>("notification_model")!!
        notificationType = intent?.getSerializableExtra("notification_type") as NotificationType


        fillUiData()
        initUiListener()
        uiVisibillityByNotificationType()
    }


    private fun fillUiData() {
        msgTextViewNotificationInfoActivity.text = notificationModel.body
        dateFromToTextViewNotificationInfoActivity.text = getNotificationDateFromToFormatter(notificationModel.item?.start_date, notificationModel.item?.end_date)

        createdDateTextViewNotificationInfoActivity.text = getNotificationDateTime(notificationModel.created_at)
        dateTextViewNotificationInfoActivity.text = getNotificationDateTime(notificationModel.created_at)

        leaveInTextViewNotificationInfoActivity.text =
            dateTimeToTime(notificationModel.item?.start_date)
        leaveOutTextViewNotificationInfoActivity.text =
            dateTimeToTime(notificationModel.item?.end_date)
        when (notificationModel.item?.status) {

            UserNoteType.PUNCH -> {
                leaveOutTextViewNotificationInfoActivity.text = ""
            }

            UserNoteType.LEAVE_REQUEST -> {

                leaveOutTextViewNotificationInfoActivity.setTextColor(Color.parseColor("#A7B811"))
                leaveOutTextViewNotificationInfoActivity.apply {
                    text = getString(R.string.pending)
                    setTextColor(Color.parseColor("#A7B811"))
                }
            }

            UserNoteType.LEAVE_APPROVED -> {
                leaveOutTextViewNotificationInfoActivity.apply {
                    text = getString(R.string.approved)
                    setTextColor(Color.parseColor("#00D865"))
                }
            }

            UserNoteType.LEAVE_REJECT -> {

                leaveOutTextViewNotificationInfoActivity.apply {
                    text = getString(R.string.rejected)
                    setTextColor(Color.parseColor("#FF0000"))
                }
            }

            UserNoteType.REVERIFY_ACCOUNT -> {
                leaveOutTextViewNotificationInfoActivity.apply {
                    text = getString(R.string.re_verify)
                    setTextColor(Color.parseColor("#3F51B5"))
                }
            }

            UserNoteType.APPROVED_ACCOUNT -> {
                leaveOutTextViewNotificationInfoActivity.apply {
                    text = getString(R.string.approved_account)
                    setTextColor(Color.parseColor("#3F51B5"))
                }
            }

            UserNoteType.DISABLED_ACCOUNT -> {
                leaveOutTextViewNotificationInfoActivity.apply {
                    text = getString(R.string.disabled_account)
                    setTextColor(Color.parseColor("#FF0000"))
                }
            }

        }
    }

    private fun initUiListener() {
        backArrowImageButtonNotificationInfoActivity.setOnClickListener {
            onBackPressed()
        }

        settingImageButtonNotificationInfoActivity.setOnClickListener {
            if(settingPopupMyLinearLayoutNotificationInfoActivity.isVisible){
                settingPopupMyLinearLayoutNotificationInfoActivity.visibility =View.GONE
            }else{
                settingPopupMyLinearLayoutNotificationInfoActivity.visibility =View.VISIBLE
            }
        }

        clearNotificationSettingPopupTextViewNotificationInfoActivity.setOnClickListener {
            notificationModel.user_id?.let { it1 -> cancelLeaveRequest(it1) }
            settingPopupMyLinearLayoutNotificationInfoActivity.visibility =View.GONE
        }

        editLeaveRequestSettingPopupTextViewNotificationInfoActivity.setOnClickListener {
            settingPopupMyLinearLayoutNotificationInfoActivity.visibility =View.GONE
            val intent = Intent(this , EditRequstLeaveActivity::class.java)
            intent.putExtra("Leave_Request_model", notificationModel.toLeaveModel())
            startActivity(intent)

        }

        deleteLeaveRequestSettingPopupTextViewNotificationInfoActivity.setOnClickListener {
            notificationModel.id?.let { it1 -> deleteNotification(it1) }
            settingPopupMyLinearLayoutNotificationInfoActivity.visibility =View.GONE
        }

        approvalButtonNotificationInfoActivity.setOnClickListener {
            notificationModel.item?.id?.let { it1 -> approveLeave(it1) }
        }

        rejectButtonNotificationInfoActivity.setOnClickListener {
            notificationModel.item?.id?.let { it1 -> rejectLeave(it1) }
        }

    }

    private fun uiVisibillityByNotificationType(){
       when(notificationType){
           NotificationType.MyNotification ->{
               statusLabelTextViewNotificationInfoActivity.visibility =View.VISIBLE
               statusTextViewNotificationInfoActivity.visibility =View.VISIBLE
               notificationStatusActionLinearLayout.visibility =View.GONE
           }
           NotificationType.MyTeamNotification ->{
               statusLabelTextViewNotificationInfoActivity.visibility =View.GONE
               statusTextViewNotificationInfoActivity.visibility =View.GONE
               notificationStatusActionLinearLayout.visibility =View.VISIBLE
           }
       }
    }

    private fun dateTimeToTime(dateTime: String?): String {
        /*
         val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formattedDate = formatter.format(parser.parse(dateTime))
         */
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("HH:mm")
            val formattedDate = formatter.format(parser.parse(dateTime))
            formattedDate
        } catch (ex: Exception) {
            dateTime ?: ""
        }
    }

    private fun dateTimeToTimeAM_PM(dateTime: String?): String {
        /*
         val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formattedDate = formatter.format(parser.parse(dateTime))
         */
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("HH:mm a")
            val formattedDate = formatter.format(parser.parse(dateTime))
            formattedDate
        } catch (ex: Exception) {
            dateTime ?: ""
        }
    }

    private fun dateTimeToDayAsString(dateTime: String?): String {
        /*
         val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formattedDate = formatter.format(parser.parse(dateTime))
         */
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd")
            val formattedDate = formatter.format(parser.parse(dateTime))
            formattedDate + "th"
        } catch (ex: Exception) {
            (dateTime + "th") ?: ""
        }
    }

    private fun dateTimeToMonthName(dateTime: String?): String {
        /*
         M -> 9
         MM -> 09
         MMM -> Sep
         MMMM -> September
         */
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("MMM")
            val formattedDate = formatter.format(parser.parse(dateTime))
            formattedDate
        } catch (ex: Exception) {
            dateTime ?: ""
        }
    }

    private fun getNotificationDateTime(dateTime: String?): String {
        var customFormat: String = ""
        return try {

            val parser = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val dayFormatter = SimpleDateFormat("dd")
            val monthFormatter = SimpleDateFormat("MMM")
            val timeFormatter = SimpleDateFormat("HH:mm a")

            val day = dayFormatter.format(parser.parse(dateTime))+"th"
            val month = monthFormatter.format(parser.parse(dateTime))
            val time = timeFormatter.format(parser.parse(dateTime))

            customFormat = "$day $month - $time"
            customFormat

        } catch (ex: Exception) {
            dateTime ?: ""
        }
    }

    private fun getNotificationDateFromToFormatter(startDate: String? , endDate:String?): String {
        var customFormat: String = ""
        return try {

            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dayFormatter = SimpleDateFormat("d")
            val monthFormatter = SimpleDateFormat("MMM")

            val startDay = dayFormatter.format(parser.parse(startDate))+"th"
            val endDay = dayFormatter.format(parser.parse(endDate))+"th"

            val startMonth = monthFormatter.format(parser.parse(startDate))
            val endMonth = monthFormatter.format(parser.parse(endDate))


            customFormat = "From $startDay $startMonth to $endDay $endMonth "
            customFormat

        } catch (ex: Exception) {
            startDate ?: ""
        }
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
                            this@NotificationInfoActivity,
                            "Successfully Canceled ",
                            Toast.LENGTH_LONG
                        ).show()

                        onBackPressed()

                    } else {
                        Toast.makeText(
                            this@NotificationInfoActivity,
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
                        this@NotificationInfoActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
               this,
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
                            this@NotificationInfoActivity,
                            "Successfully Deleted",
                            Toast.LENGTH_LONG
                        ).show()

                        onBackPressed()

                    } else {
                        Toast.makeText(
                           this@NotificationInfoActivity,
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
                        this@NotificationInfoActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                this@NotificationInfoActivity,
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
                            this@NotificationInfoActivity,
                            "Successfully Approved",
                            Toast.LENGTH_LONG
                        ).show()

                        onBackPressed()

                    } else {
                        Toast.makeText(
                            this@NotificationInfoActivity,
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
                        this@NotificationInfoActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                this@NotificationInfoActivity,
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
                            this@NotificationInfoActivity,
                            "Successfully Rejected",
                            Toast.LENGTH_LONG
                        ).show()

                        onBackPressed()
                    } else {
                        Toast.makeText(
                            this@NotificationInfoActivity,
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
                        this@NotificationInfoActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                this@NotificationInfoActivity,
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}