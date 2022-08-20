package com.example.myapplication.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.MyLeaveHistoryModel
import com.example.myapplication.myapplication.models.MyTeamLeaveHistoryModel
import com.google.android.material.card.MaterialCardView
import java.util.ArrayList

class MyLeavesHistoryAdapter() :
    RecyclerView.Adapter<MyLeavesHistoryAdapter.ViewHolder>() {

    private val arrayList: ArrayList<MyLeaveHistoryModel> = ArrayList()
    var editLeaveRequestOnItemClick: ((MyLeaveHistoryModel?) -> Unit)? = null
    var deleteLeaveRequestOnItemClick: ((MyLeaveHistoryModel?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_leave_history_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val model =arrayList?.get(holder.adapterPosition)
        holder.titleDateTextView.text =
            DateUtils().getDateForaAdapterFromTimeStamp(model?.start_date)
        holder.titleInTextView.text =  DateUtils().getLeaveForaAdapterFromTimeStamp(model?.start_date)
        holder.titleOutTextView.text =  DateUtils().getLeaveForaAdapterFromTimeStamp(model?.end_date)
        holder.titleBreakTextView.text = model?.status

        if (model?.status?.contains("pending") == true) {
            holder.setting.visibility =View.VISIBLE
        } else {
            holder.setting.visibility =View.GONE
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    fun submitData(list: ArrayList<MyLeaveHistoryModel>) {
        arrayList.clear()
        arrayList.addAll(list)
    }

    inner  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDateTextView: TextView
        var titleInTextView: TextView
        var titleOutTextView: TextView
        var titleBreakTextView: TextView
        var setting: ImageButton
        var editLeaveRequestPopup: Button
        var deleteLeaveRequestPopup: Button

        var leaveRequestPopup: MaterialCardView

        init {
            titleDateTextView = view.findViewById<View>(R.id.titleDateTextView) as TextView
            titleInTextView = view.findViewById<View>(R.id.titleInTextView) as TextView
            titleOutTextView = view.findViewById<View>(R.id.titleOutTextView) as TextView
            titleBreakTextView = view.findViewById<View>(R.id.titleBreakTextView) as TextView
            setting = view.findViewById<View>(R.id.settingImageButtonMyLeaveHistoryRow) as ImageButton

            editLeaveRequestPopup = view.findViewById<View>(R.id.editLeaveRequestPopup) as Button
            deleteLeaveRequestPopup = view.findViewById<View>(R.id.deleteLeaveRequestPopup) as Button

            leaveRequestPopup = view.findViewById<View>(R.id.settingsPopupMaterialCardView) as MaterialCardView


            editLeaveRequestPopup.setOnClickListener {
                editLeaveRequestOnItemClick?.invoke(arrayList?.get(adapterPosition))
                leaveRequestPopup.visibility = View.GONE
            }

            deleteLeaveRequestPopup.setOnClickListener {
                deleteLeaveRequestOnItemClick?.invoke(arrayList?.get(adapterPosition))
                leaveRequestPopup.visibility = View.GONE
            }
        }
    }

}