package com.example.myapplication.myapplication.models

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import org.json.JSONException

@Parcelize
data class MyTeamLeaveHistoryModel(
    val id: Int? = null,
    val user_id: Int? = null,
    val amount_in_min: String? = null,
    val start_date: String? = null,
    val end_date: String? = null,
    val type_id: Int? = null,
    val status: String? = null,
    val attachment: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val user: User? = null,
    val leave_type: LeaveType? = null,
) : Parcelable {
/*    @Throws(JSONException::class)
    fun parseArray(data: String): ArrayList<MyTeamLeaveHistoryModel?>? {
        val gson: Gson = GsonBuilder().create()
        val listType = object :
            TypeToken<List<MyTeamLeaveHistoryModel?>?>() {}.type
        return gson.fromJson(data.toString(), listType)
    }*/
}

@Parcelize
data class User(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val email_verified_at: String? = null,
    val two_factor_confirmed_at: String? = null,
    val current_team_id: Int? = null,
    val start_working_date: String? = null,
    val shift_id: Int? = null,
    val profile_photo_path: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val status: Int? = null,
    val location_id: Int? = null,
    val reported_to: Int? = null,
    val lang: String? = null,
    val device_id: String? = null,
    val mobile_number: String? = null,
    val company_code: String? = null,
    val profile_photo_url: String? = null,
    val video: String? = null,
): Parcelable


@Parcelize
data class LeaveType(
    val id: Int? = null,
    val name_en: String? = null,
    val name_ar: String? = null,
    val has_attachment: Int? = null,
    val has_balance: Int? = null,
    val is_free: Int? = null,
    val balance_type_id: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val name: String? = null,
): Parcelable


/*

        "leave_type": {

            "": "2022-08-08T20:18:07.000000Z",
            "": "2022-08-08T20:18:07.000000Z",
            "": "personal"
        }
    },
 */

