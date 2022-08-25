package com.example.myapplication.myapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(
    val id:Int?= null,
    val user_id:Int?= null,
    val amount_in_min:String?= null,
    val start_date:String?= null,
    val end_date:String?= null,
    val type_id:Int?= null,
    val status:String?= null,//pending
    val attachment:String?= null,
    val created_at:String?= null,
    val updated_at:String?= null,
):Parcelable
