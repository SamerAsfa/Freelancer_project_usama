package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject

@Parcelize
data class NotificationModel(
    var id: String? = null,
    var user_id: Int? = null,
    var not_type: String? = null,//leaveRequest
    var item_id: String? = null,
    val title: String? = null,//leaveRequest
    val body: String? = null,//leaveRequest
    val is_read: Int? = 1,
    val status: String? = null,//pending
    val created_at: String? = null, //24/08/2022 19:34
    val updated_at: String? = null,//2022-08-24T19:34:40.000000Z
    val item: ItemModel? = null,
    val user: UserModel? = null,
) : Parcelable {
    @Throws(JSONException::class)
    fun parse(data: JSONObject): NotificationModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data.toString(), NotificationModel::class.java)
    }

    @Throws(JSONException::class)
    fun parse(data: String): NotificationModel? {
        val gson: Gson = GsonBuilder().create()
        return gson.fromJson(data, NotificationModel::class.java)
    }

    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<NotificationModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<NotificationModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }


    companion object {
        fun NotificationModel.toLeaveModel(): MyLeaveHistoryModel {
            return MyLeaveHistoryModel(
                id = this.item?.id,
                user_id = this.item?.user_id,
                date = null,
                pin = null,
                pout = null,
                amount_in_min = this.item?.amount_in_min,
                start_date = this.item?.start_date,
                end_date = this.item?.end_date,
                type_id = this.item?.type_id,
                status = this.item?.status,
                attachment = this.item?.attachment,
                created_at = this.item?.created_at,
                updated_at = this.item?.updated_at,
            )
        }
    }
}