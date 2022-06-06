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


class HistoryAdapter(private val arrayList: ArrayList<HistoryModel>?) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


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
            DateUtils().getDateForaAdapterFromTimeStamp(arrayList?.get(holder.adapterPosition)?.date)

        if (!arrayList?.get(holder.adapterPosition)?.pin.isNullOrBlank()) {
            holder.titleInTextView.text =
                arrayList?.get(holder.adapterPosition)?.pin?.toLong()?.let {
                    DateUtils().getTime(
                        it
                    )
                }
        }
        if (!arrayList?.get(holder.adapterPosition)?.pout.isNullOrBlank()) {
            holder.titleOutTextView.text =
                arrayList?.get(holder.adapterPosition)?.pout?.toLong()?.let {
                    DateUtils().getTime(
                        it
                    )
                }
        }
        val hours = arrayList?.get(holder.adapterPosition)?.breaks?.div(60)
        val minutes = arrayList?.get(holder.adapterPosition)?.breaks?.rem(60)
        val time = String.format("%d:%02d", hours, minutes)
        if (arrayList?.get(holder.adapterPosition)?.breaks == 0) {
            holder.titleBreakTextView.text = "--:--"
        } else {
            holder.titleBreakTextView.text = time
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

        init {
            titleDateTextView = view.findViewById<View>(R.id.titleDateTextView) as TextView
            titleInTextView = view.findViewById<View>(R.id.titleInTextView) as TextView
            titleOutTextView = view.findViewById<View>(R.id.titleOutTextView) as TextView
            titleBreakTextView = view.findViewById<View>(R.id.titleBreakTextView) as TextView
        }
    }

}