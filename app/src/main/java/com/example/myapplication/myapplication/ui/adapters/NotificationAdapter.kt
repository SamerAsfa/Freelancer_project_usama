package com.example.myapplication.myapplication.ui.adapters


import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.common.UserNoteType
import com.example.myapplication.myapplication.common.getAbbreviatedFromDateTime
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.models.MyLeaveHistoryModel
import com.example.myapplication.myapplication.models.MyTeamLeaveHistoryModel
import com.example.myapplication.myapplication.models.NotificationModel
import java.util.*

class NotificationAdapter() :  RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val arrayList: ArrayList<NotificationModel> = ArrayList()
    var notificationSettingsOnItemClick: ((NotificationModel?) -> Unit)? = null
    var clearNotificationOnItemClick: ((NotificationModel?) -> Unit)? = null
    var editLeaveRequestOnItemClick: ((NotificationModel?) -> Unit)? = null
    var deleteLeaveRequestOnItemClick: ((NotificationModel?) -> Unit)? = null
    var onItemClick: ((NotificationModel?) -> Unit)? = null

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
        val position = holder.adapterPosition
        val model: NotificationModel? =arrayList?.get(position)

       // val date:String = "".getAbbreviatedFromDateTime(arrayList?.get(position)?.created_at!!,"dd/MM/yyyy HH:mm","MMMM")
        holder.dateTextView.text =model?.created_at

        model?.user?.profile_photo_url?.let {
            Glide.with(holder.userImage.context)
                .load(it)
                .placeholder(R.drawable.vector_personal)
                .into(holder.userImage)
        }


 /*       holder.dateTextView.text =
            arrayList?.get(position)?.updated_at?.indexOf('T')
                ?.let { lastPositionToSubString ->
                    arrayList[position].updated_at?.substring(0 , lastPositionToSubString)
                }*/


 /*        holder.dateTextView.text = String.format(
             Locale.CANADA,
             "%sth %s",
             DateUtils().TMonthformatUtc(arrayList?.get(position)?.created_at),
             DateUtils().TTimeformatUtc(arrayList?.get(position)?.created_at)
         )*/

        when(model?.not_type){

            UserNoteType.PUNCH ->{
                holder.stateTextView.visibility =View.INVISIBLE
            }

            UserNoteType.LEAVE_REQUEST ->{

                holder.msgTextView.text = "Applied for Leave Request."

                holder.stateTextView.apply {
                    text =holder.stateTextView.context.getString(R.string.approval_pending)
                    setTextColor( Color.parseColor("#FFFFFF"))
                    textSize =8f
                    background =holder.stateTextView.context.getDrawable(R.drawable.approved_pending)
                }
            }

            UserNoteType.LEAVE_APPROVED ->{

                holder.msgTextView.text = " HR Approved Your Registration Request."

                holder.stateTextView.apply {
                    text =holder.stateTextView.context.getString(R.string.approved)
                    setTextColor( Color.parseColor("#00D865"))
                    textSize =12f
                    background =null
                }
            }

            UserNoteType.LEAVE_REJECT ->{

                holder.msgTextView.text = "Applied for Leave Request"

                holder.stateTextView.apply {
                    text =holder.stateTextView.context.getString(R.string.rejected)
                    setTextColor( Color.parseColor("#FF0000"))
                    textSize =12f
                    background =null
                }
            }

            UserNoteType.REVERIFY_ACCOUNT ->{

                holder.msgTextView.text = "Your Registration Request needs Re-Verify."

                holder.stateTextView.apply {
                    text =holder.stateTextView.context.getString(R.string.re_verify)
                    setTextColor( Color.parseColor("#FFFFFF"))
                    textSize =8f
                    background =holder.stateTextView.context.getDrawable(R.drawable.re_verify)
                }
            }

            UserNoteType.APPROVED_ACCOUNT ->{
                holder.stateTextView.visibility =View.INVISIBLE
            }

            UserNoteType.DISABLED_ACCOUNT ->{

                val disabled = getColoredSpanned("Disabled", "#EC1119")

                holder.msgTextView.setText(Html.fromHtml("Your Account has been"+" "+disabled +" by HR manager."))
               // holder.msgTextView.text = context.getString(R.string.account_disabled)//"Your Account has been Disabled by HR manager."
                holder.stateTextView.visibility =View.INVISIBLE
            }

        }


        /*** popup setting permission
        * if notification status approved juist access to clear notification
        * else if notification status not approved like pending  you cal access
         1- clear notification
         2- edit leave request
         3- delete leave request
        ***/
        when(model?.not_type){

            UserNoteType.PUNCH ->{}

            UserNoteType.LEAVE_REQUEST ->{

                holder.cleareNotificationSettingPopup.visibility =View.VISIBLE
                holder.editLeaveRequestSettingPopup.visibility =View.VISIBLE
                holder.deleteLeaveRequestSettingPopup.visibility =View.VISIBLE
            }

            UserNoteType.LEAVE_APPROVED ->{

                holder.cleareNotificationSettingPopup.visibility =View.VISIBLE
                holder.editLeaveRequestSettingPopup.visibility =View.GONE
                holder.deleteLeaveRequestSettingPopup.visibility =View.GONE
            }

            UserNoteType.LEAVE_REJECT ->{

                holder.cleareNotificationSettingPopup.visibility =View.VISIBLE
                holder.editLeaveRequestSettingPopup.visibility =View.GONE
                holder.deleteLeaveRequestSettingPopup.visibility =View.GONE
            }

            UserNoteType.REVERIFY_ACCOUNT ->{

                holder.cleareNotificationSettingPopup.visibility =View.VISIBLE
                holder.editLeaveRequestSettingPopup.visibility =View.GONE
                holder.deleteLeaveRequestSettingPopup.visibility =View.GONE
            }

            UserNoteType.APPROVED_ACCOUNT ->{
                holder.cleareNotificationSettingPopup.visibility =View.VISIBLE
                holder.editLeaveRequestSettingPopup.visibility =View.GONE
                holder.deleteLeaveRequestSettingPopup.visibility =View.GONE
            }

            UserNoteType.DISABLED_ACCOUNT ->{

                holder.cleareNotificationSettingPopup.visibility =View.VISIBLE
                holder.editLeaveRequestSettingPopup.visibility =View.GONE
                holder.deleteLeaveRequestSettingPopup.visibility =View.GONE
            }

        }

    }

    fun submitData(list: ArrayList<NotificationModel>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userImage: ImageView
        var msgTextView: TextView
        var dateTextView: TextView
        var stateTextView: TextView
        var notificationRowSettingImageButton: ImageButton
        var notificationSettingPopup: LinearLayout
        var cleareNotificationSettingPopup: TextView
        var editLeaveRequestSettingPopup: TextView
        var deleteLeaveRequestSettingPopup: TextView

        init {
            userImage = view.findViewById<View>(R.id.userImageViewNotificationItemRow) as ImageView
            msgTextView = view.findViewById<View>(R.id.msgTextView) as TextView
            dateTextView = view.findViewById<View>(R.id.dateTextView) as TextView
            stateTextView = view.findViewById<View>(R.id.stateTextView) as TextView
            notificationRowSettingImageButton = view.findViewById<View>(R.id.notificationSettingsImageButton) as ImageButton
            notificationSettingPopup = view.findViewById<View>(R.id.notificationItemRowSettingsLinearLayout) as LinearLayout
            cleareNotificationSettingPopup = view.findViewById<View>(R.id.clearNotificationSettingPopupTextViewNotificationItemRow) as TextView
            editLeaveRequestSettingPopup = view.findViewById<View>(R.id.editLeaveRequestSettingPopupTextViewNotificationItemRow) as TextView
            deleteLeaveRequestSettingPopup = view.findViewById<View>(R.id.deleteLeaveRequestSettingPopupTextViewNotificationItemRow) as TextView

            notificationRowSettingImageButton.setOnClickListener {
                if(notificationSettingPopup.isVisible){
                    notificationSettingPopup.visibility = View.GONE
                }else {
                    notificationSettingsOnItemClick?.invoke(arrayList?.get(adapterPosition))
                    notificationSettingPopup.visibility = View.VISIBLE
                }
            }

            cleareNotificationSettingPopup.setOnClickListener {
                clearNotificationOnItemClick?.invoke(arrayList[adapterPosition])
                notificationSettingPopup.visibility = View.GONE
            }

            editLeaveRequestSettingPopup.setOnClickListener {
                editLeaveRequestOnItemClick?.invoke(arrayList[adapterPosition])
                notificationSettingPopup.visibility = View.GONE
            }

            deleteLeaveRequestSettingPopup.setOnClickListener {
                deleteLeaveRequestOnItemClick?.invoke(arrayList[adapterPosition])
                notificationSettingPopup.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(arrayList[adapterPosition])
            }

        }
    }

    fun clearData(){
        arrayList.clear()
        notifyDataSetChanged()
    }
    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }
}