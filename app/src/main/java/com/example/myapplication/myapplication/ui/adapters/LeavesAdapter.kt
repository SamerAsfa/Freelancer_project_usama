package com.example.myapplication.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.HistoryModel
import java.util.ArrayList

class LeavesAdapter(private val arrayList: ArrayList<HistoryModel>?) :
    RecyclerView.Adapter<LeavesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_history, parent, false)
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
            titleDateTextView = view.findViewById<View>(R.id.titleDateTextView) as TextView
            titleInTextView = view.findViewById<View>(R.id.titleInTextView) as TextView
            titleOutTextView = view.findViewById<View>(R.id.titleOutTextView) as TextView
            titleBreakTextView = view.findViewById<View>(R.id.titleBreakTextView) as TextView
        }
    }

}