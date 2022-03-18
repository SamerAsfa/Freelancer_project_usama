package com.example.myapplication.myapplication.presentation

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.models.ItemViewModel
import com.squareup.moshi.Json


class UserListingViewModel(
    val id: String,
    val name: String,
    @Json(name = "profile_pic_url") val profilePicUrl: String,
) : ItemViewModel {

    override val layoutId: Int = R.layout.item_user_listing

    override val viewType: Int = UserListViewModel.LISTING_ITEM



    companion object {
        @BindingAdapter("image")
        @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String) {
//            Glide.with(view.context).setDefaultRequestOptions(RequestOptions().circleCrop())
//                .load(imageUrl).apply( RequestOptions().override(80, 80)).into(view)
        }
    }
}