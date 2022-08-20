package com.example.myapplication.myapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.MyTeamLeaveHistoryModel
import com.google.android.material.card.MaterialCardView
import java.util.ArrayList

class MyTeamLeavesHistoryAdapter(
    private val context: Context
) : RecyclerView.Adapter<MyTeamLeavesHistoryAdapter.ViewHolder>() {

    private val arrayList: ArrayList<MyTeamLeaveHistoryModel> = ArrayList()
    var approvedOnItemClick: ((MyTeamLeaveHistoryModel?) -> Unit)? = null
    var rejectedOnItemClick: ((MyTeamLeaveHistoryModel?) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_team_leave_history_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model = arrayList?.get(holder.adapterPosition)

        holder.titleDateTextView.text =
            DateUtils().getDateForaAdapterFromTimeStamp(model?.start_date)
        holder.titleInTextView.text =
            DateUtils().getLeaveForaAdapterFromTimeStamp(model?.start_date)
        holder.titleOutTextView.text = DateUtils().getLeaveForaAdapterFromTimeStamp(model?.end_date)
        holder.titleBreakTextView.text = model?.status

        holder.employeeNameSettingsPopup.text = model?.user?.name

        if (!model?.user?.profile_photo_url?.trim()?.isBlank()!!) {
            Glide.with(context)
                .load(model.user.profile_photo_url).into(holder.userImage)
        }

        if (model.status?.contains("pending") == true) {
             holder.settings2.visibility =View.VISIBLE
        } else {
            holder.settings2.visibility =View.GONE
        }

        holder.settings.setOnClickListener {
            if (holder.settings1Popup.isVisible) {
                holder.settings1Popup.visibility = View.GONE
            } else {
                holder.settings1Popup.visibility = View.VISIBLE
            }
        }

        holder.settings2.setOnClickListener {
            if (holder.settings2Popup.isVisible) {
                holder.settings2Popup.visibility = View.GONE
            } else {
                holder.settings2Popup.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    fun submitData(list: ArrayList<MyTeamLeaveHistoryModel>) {
        arrayList.clear()
        arrayList.addAll(list)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDateTextView: TextView
        var titleInTextView: TextView
        var titleOutTextView: TextView
        var titleBreakTextView: TextView
        var userImage: ImageView
        var settings: ImageButton
        var settings2: ImageButton
        var employeeNameSettingsPopup: TextView
        var settings1Popup: MaterialCardView
        var settings2Popup: MaterialCardView
        var settingPopupApprovedButton: Button
        var settingPopupRejectedButton: Button

        init {
            titleDateTextView =
                view.findViewById<View>(R.id.myTeamLeaveTitleDateTextView) as TextView
            titleInTextView = view.findViewById<View>(R.id.myTeamLeaveTitleInTextView) as TextView
            titleOutTextView = view.findViewById<View>(R.id.myTeamLeaveTitleOutTextView) as TextView
            titleBreakTextView =
                view.findViewById<View>(R.id.myTeamLeaveTitleBreakTextView) as TextView
            userImage = view.findViewById<View>(R.id.userImageMyTeamLeaveHistoryRow) as ImageView
            settings =
                view.findViewById<View>(R.id.settingImageButtonMyTeamLeaveHistoryRow) as ImageButton
            settings2 =
                view.findViewById<View>(R.id.setting1ImageButtonMyTeamLeaveHistoryRow) as ImageButton

            employeeNameSettingsPopup =
                view.findViewById<View>(R.id.employeeNameTextViewPopupMyTeamLeaveHistoryRow) as TextView
            settings1Popup =
                view.findViewById<View>(R.id.employeeSettingsPopupMaterialCardView) as MaterialCardView
            settings2Popup =
                view.findViewById<View>(R.id.settings2PopupMaterialCardViewMyTeamLeaveHistoryRow) as MaterialCardView

            settingPopupApprovedButton =
                view.findViewById<View>(R.id.approvalButtonMyTeamLeaveHistoryRow) as Button
            settingPopupRejectedButton =
                view.findViewById<View>(R.id.rejectedButtonMyTeamLeaveHistoryRow) as Button



            settingPopupApprovedButton.setOnClickListener {
                approvedOnItemClick?.invoke(arrayList?.get(adapterPosition))
                settings2Popup.visibility = View.GONE
            }

            settingPopupRejectedButton.setOnClickListener {
                rejectedOnItemClick?.invoke(arrayList?.get(adapterPosition))
                settings2Popup.visibility = View.GONE
            }

        }
    }

}