package com.example.myapplication.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.MyTeamLeaveHistoryModel
import java.util.ArrayList

class MyTeamLeavesHistoryAdapter(private val arrayList: ArrayList<MyTeamLeaveHistoryModel>?) :
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
        holder.titleDateTextView.text =
            DateUtils().getDateForaAdapterFromTimeStamp(arrayList?.get(holder.adapterPosition)?.start_date)
        holder.titleInTextView.text =  DateUtils().getLeaveForaAdapterFromTimeStamp(arrayList?.get(holder.adapterPosition)?.start_date)
        holder.titleOutTextView.text =  DateUtils().getLeaveForaAdapterFromTimeStamp(arrayList?.get(holder.adapterPosition)?.end_date)
        holder.titleBreakTextView.text = arrayList?.get(holder.adapterPosition)?.status
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDateTextView: TextView
        var titleInTextView: TextView
        var titleOutTextView: TextView
        var titleBreakTextView: TextView

        init {
            titleDateTextView = view.findViewById<View>(R.id.myTeamLeaveTitleDateTextView) as TextView
            titleInTextView = view.findViewById<View>(R.id.myTeamLeaveTitleInTextView) as TextView
            titleOutTextView = view.findViewById<View>(R.id.myTeamLeaveTitleOutTextView) as TextView
            titleBreakTextView = view.findViewById<View>(R.id.myTeamLeaveTitleBreakTextView) as TextView
        }
    }

}