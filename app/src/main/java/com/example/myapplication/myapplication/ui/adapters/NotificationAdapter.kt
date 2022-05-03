package com.example.myapplication.myapplication.ui.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.models.NotificationModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.NonDisposableHandle.parent

class NotificationAdapter(private val arrayList: ArrayList<NotificationModel?>?) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.msgTextView.text = arrayList?.get(holder.adapterPosition)?.title
        holder.dateTextView.text = arrayList?.get(holder.adapterPosition)?.date
        holder.stateTextView.text = arrayList?.get(holder.adapterPosition)?.not_type
        val userModel = LongTermManager.getInstance().userModel
        if (!userModel?.profile_photo_path?.trim()?.isBlank()!!) {
//            Glide.with()
//                .load(userModel?.profile_photo_path).into(view.userImage)
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userImage: ImageView
        var msgTextView: TextView
        var dateTextView: TextView
        var stateTextView: TextView

        init {
            userImage = view.findViewById<View>(R.id.userImage) as ImageView
            msgTextView = view.findViewById<View>(R.id.msgTextView) as TextView
            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView
            stateTextView = view.findViewById<View>(R.id.stateTextView) as TextView
        }
    }

}