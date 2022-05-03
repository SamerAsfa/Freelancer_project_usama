package com.example.myapplication.myapplication.services

import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.NotificationModel
import com.example.myapplication.myapplication.notifications.NotificationsManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class VocoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // message, here is where that should be initiated. See SendNotificationAsync method below.
        val arrayList = ArrayList<String>()

        val data: Map<String, String> = remoteMessage.getData()
        val json = JSONObject()
        remoteMessage.data.keys.forEach { key->
            if(key=="body"){
                json.put("body",remoteMessage.data.getValue(key))
            }else if(key=="title"){
                json.put("title",remoteMessage.data.getValue(key))
            }else if(key=="click_action"){
                json.put("click_action",remoteMessage.data.getValue(key))
            }else if(key=="not_type"){
                json.put("not_type",remoteMessage.data.getValue(key))
            }
        }
        json.put("date",DateUtils().getDateFromTimeStampNotification(Date().time))
        if (remoteMessage.data.keys.size > 0) {
            try {
                val notificationModel: NotificationModel? = NotificationModel().parse(json)
                notificationModel?.let { sendNotification(it) }
            } catch (ex: java.lang.Exception) {
                ex.message
            }
        }
    }

    override fun onNewToken( token : String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        try {
            LongTermManager.getInstance().notificationsToken = token
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // [END receive_message]

    // [END receive_message]
    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private fun sendNotification(model: NotificationModel) {
        try {
            val array = LongTermManager.getInstance().notificationModelArrayList
            array.add(model)
            LongTermManager.getInstance().notificationModelArrayList = array
            val notificationsManager = NotificationsManager()
            notificationsManager.showNotification(this, model)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}