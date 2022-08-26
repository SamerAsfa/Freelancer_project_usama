package com.example.myapplication.myapplication.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.common.UserNoteType
import com.example.myapplication.myapplication.models.MyLeaveHistoryModel
import com.example.myapplication.myapplication.models.NotificationModel
import kotlinx.android.synthetic.main.activity_notification_info.*


class NotificationInfoActivity : AppCompatActivity() {

    lateinit var notificationModel: NotificationModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_info)

        notificationModel = intent.extras?.getParcelable<NotificationModel>("notification_model")!!

        fillUiData()
        initUiListener()
    }


    private fun fillUiData(){
        msgTextViewNotificationInfoActivity.text = notificationModel.body
        dateFromToTextViewNotificationInfoActivity.text = notificationModel.title
        createdDateTextViewNotificationInfoActivity.text = notificationModel.created_at
        dateTextViewNotificationInfoActivity.text = notificationModel.created_at
        leaveInTextViewNotificationInfoActivity.text = notificationModel.item?.start_date
        leaveOutTextViewNotificationInfoActivity.text = notificationModel.item?.end_date
        when(notificationModel.item?.status){

            UserNoteType.PUNCH ->{
                leaveOutTextViewNotificationInfoActivity.text = ""
            }

            UserNoteType.LEAVE_REQUEST ->{
                leaveOutTextViewNotificationInfoActivity.setTextColor(Color.parseColor("#A7B811"))
                leaveOutTextViewNotificationInfoActivity.apply {
                    text =getString(R.string.pending)
                    setTextColor( Color.parseColor("#A7B811"))
                }
            }

            UserNoteType.LEAVE_APPROVED ->{
                leaveOutTextViewNotificationInfoActivity.apply {
                    text =getString(R.string.approved)
                    setTextColor( Color.parseColor("#00D865"))
                }
            }

            UserNoteType.LEAVE_REJECT ->{

                leaveOutTextViewNotificationInfoActivity.apply {
                    text =getString(R.string.rejected)
                    setTextColor( Color.parseColor("#FF0000"))
                }
            }

            UserNoteType.REVERIFY_ACCOUNT ->{
                leaveOutTextViewNotificationInfoActivity.apply {
                    text =getString(R.string.re_verify)
                    setTextColor( Color.parseColor("#3F51B5"))
                }
            }

            UserNoteType.APPROVED_ACCOUNT ->{
                leaveOutTextViewNotificationInfoActivity.apply {
                    text =getString(R.string.approved_account)
                    setTextColor( Color.parseColor("#3F51B5"))
                }
            }

            UserNoteType.DISABLED_ACCOUNT ->{
                leaveOutTextViewNotificationInfoActivity.apply {
                    text =getString(R.string.disabled_account)
                    setTextColor( Color.parseColor("#FF0000"))
                }
            }

        }

    }
    private fun initUiListener(){
        backArrowImageButtonNotificationInfoActivity.setOnClickListener {
            onBackPressed()
        }

    }
}