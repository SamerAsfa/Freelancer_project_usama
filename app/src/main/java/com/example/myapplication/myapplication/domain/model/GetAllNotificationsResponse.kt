package com.example.myapplication.myapplication.domain.model

import com.example.myapplication.myapplication.models.NotificationModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAllNotificationsResponse(

    @SerializedName("unread")
    @Expose
    val unread: Int? = 0,

    @SerializedName("myTeamUnread")
    @Expose
    val myTeamUnread: Int? = 0,
    
    @SerializedName("notifications")
    @Expose
    val notifications: List<NotificationModel>? = emptyList()
)
