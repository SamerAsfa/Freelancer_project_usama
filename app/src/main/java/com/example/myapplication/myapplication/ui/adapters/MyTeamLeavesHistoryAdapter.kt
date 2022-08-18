package com.example.myapplication.myapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.MyTeamLeaveHistoryModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.ArrayList

class MyTeamLeavesHistoryAdapter(private val arrayList: ArrayList<MyTeamLeaveHistoryModel>? ,
private val context: Context
) :
    RecyclerView.Adapter<MyTeamLeavesHistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_team_leave_history_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model= arrayList?.get(holder.adapterPosition)

        holder.titleDateTextView.text =
            DateUtils().getDateForaAdapterFromTimeStamp(model?.start_date)
        holder.titleInTextView.text =  DateUtils().getLeaveForaAdapterFromTimeStamp(model?.start_date)
        holder.titleOutTextView.text =  DateUtils().getLeaveForaAdapterFromTimeStamp(model?.end_date)
        holder.titleBreakTextView.text = model?.status

        if (!model?.user?.profile_photo_url?.trim()?.isBlank()!!) {
            Glide.with(context)
                .load(model.user.profile_photo_url).into(holder.userImage)
        }

        if(model.status?.contains("pending") == true){
            holder.settings1.visibility =View.VISIBLE
        }else{
            holder.settings1.visibility =View.GONE
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDateTextView: TextView
        var titleInTextView: TextView
        var titleOutTextView: TextView
        var titleBreakTextView: TextView
        var userImage: ImageView
        var settings: ImageButton
        var settings1: ImageButton

        init {
            titleDateTextView = view.findViewById<View>(R.id.myTeamLeaveTitleDateTextView) as TextView
            titleInTextView = view.findViewById<View>(R.id.myTeamLeaveTitleInTextView) as TextView
            titleOutTextView = view.findViewById<View>(R.id.myTeamLeaveTitleOutTextView) as TextView
            titleBreakTextView = view.findViewById<View>(R.id.myTeamLeaveTitleBreakTextView) as TextView
            userImage = view.findViewById<View>(R.id.userImageMyTeamLeaveHistoryRow) as ImageView
            settings = view.findViewById<View>(R.id.settingImageButtonMyTeamLeaveHistoryRow) as ImageButton
            settings1 = view.findViewById<View>(R.id.setting1ImageButtonMyTeamLeaveHistoryRow) as ImageButton
        }
    }

}