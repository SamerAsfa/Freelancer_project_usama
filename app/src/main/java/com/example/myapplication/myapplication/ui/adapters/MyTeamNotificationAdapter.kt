package com.example.myapplication.myapplication.ui.adapters


import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.common.UserNoteType
import com.example.myapplication.myapplication.models.NotificationModel
import java.util.ArrayList

class MyTeamNotificationAdapter(
) : RecyclerView.Adapter<MyTeamNotificationAdapter.ViewHolder>() {

    private val arrayList: ArrayList<NotificationModel> = ArrayList()

    var approvedOnItemClick: ((NotificationModel?) -> Unit)? = null
   var rejectOnItemClick: ((NotificationModel?) -> Unit)? = null
    var deleteNotificationSettingsOnItemClick: ((NotificationModel?) -> Unit)? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_team_notification_row, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val position = holder.adapterPosition
        val notificationModel: NotificationModel? = arrayList.get(position)

        notificationModel?.user?.profile_photo_url.let {
            Glide.with(holder.userImage.context)
                .load(it).into(holder.userImage)
        }
        holder.nameTextView.text =notificationModel?.user?.name.toString()

        holder.dateFromToTextTextView.text =   "From ${notificationModel?.item?.start_date} to ${notificationModel?.item?.end_date}"
        holder.dateTextView.text =
            notificationModel?.updated_at?.indexOf('T')
                ?.let { lastPositionToSubString ->
                    arrayList[position].updated_at?.substring(0 , lastPositionToSubString)
                }

        holder.descriptionTextView.text = notificationModel?.body

       when(notificationModel?.not_type){

            UserNoteType.PUNCH ->{
                holder.statusLinearLayout.visibility =View.INVISIBLE
                holder.watchedStatusLinearLayout.visibility =View.GONE
            }

            UserNoteType.LEAVE_REQUEST ->{

              holder.descriptionTextView.text = "Applied for Leave Request."
              holder.statusLinearLayout.visibility =View.VISIBLE
              holder.watchedStatusLinearLayout.visibility =View.GONE

            }

            UserNoteType.LEAVE_APPROVED ->{

                holder.descriptionTextView.text = " HR Approved Your Registration Request."
                holder.statusLinearLayout.visibility =View.GONE
                holder.watchedStatusLinearLayout.visibility =View.VISIBLE


                holder.statusTextView.apply {
                    text =context.getString(R.string.approved)
                    setTextColor( Color.parseColor("#00D865"))
                    textSize =14f
                    background =null
                }
            }

            UserNoteType.LEAVE_REJECT ->{
                holder.descriptionTextView.text = "Applied for Leave Request"
                holder.statusLinearLayout.visibility =View.GONE
                holder.watchedStatusLinearLayout.visibility =View.VISIBLE

                holder.statusTextView.apply {
                    text = context.getString(R.string.rejected)
                    setTextColor( Color.parseColor("#FF0000"))
                    textSize =14f
                    background =null
                }
            }

            UserNoteType.REVERIFY_ACCOUNT ->{
                holder.descriptionTextView.text = "Your Registration Request needs Re-Verify."
                holder.statusLinearLayout.visibility =View.INVISIBLE
                holder.watchedStatusLinearLayout.visibility =View.GONE

               /* holder.msgTextView.text = "Your Registration Request needs Re-Verify."

                holder.stateTextView.apply {
                    text =context.getString(R.string.approved)
                    setTextColor( Color.parseColor("#FFFFFF"))
                    textSize =8f
                    background =context.getDrawable(R.drawable.re_verify)
                }*/
            }

            UserNoteType.APPROVED_ACCOUNT ->{}

            UserNoteType.DISABLED_ACCOUNT ->{

                val disabled = getColoredSpanned("Disabled", "#EC1119")

                holder.descriptionTextView.setText(Html.fromHtml("Your Account has been Disabled "+" "+disabled +" by HR manager."))
               // holder.msgTextView.text = context.getString(R.string.account_disabled)//"Your Account has been Disabled by HR manager."

                holder.statusLinearLayout.visibility =View.INVISIBLE
                holder.watchedStatusLinearLayout.visibility =View.GONE
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
        var nameTextView: TextView
        var descriptionTextView: TextView
        var dateFromToTextTextView: TextView
        var dateTextView: TextView
        var statusLinearLayout: LinearLayout
        var watchedStatusLinearLayout: LinearLayout
        var statusTextView: TextView
        var notificationSettings: ImageButton
        var approveButton: Button
        var rejectButton: Button
        var settingPopup: LinearLayout
        var clearNotificationSettingsInPopup: TextView

        init {
            userImage = view.findViewById<View>(R.id.imageViewMyTeamNotificationRow) as ImageView
            nameTextView =
                view.findViewById<View>(R.id.employeeNameTextViewMyTeamNotificationRow) as TextView
            descriptionTextView =
                view.findViewById<View>(R.id.statusDescriptionTextViewMyTeamNotificationRow) as TextView
            dateFromToTextTextView =
                view.findViewById<View>(R.id.dateFromToTextViewMyTeamNotificationRow) as TextView
            dateTextView =
                view.findViewById<View>(R.id.dateTextViewMyTeamNotificationRow) as TextView
            statusLinearLayout =
                view.findViewById<View>(R.id.notificationStatusLinearLayout) as LinearLayout

            watchedStatusLinearLayout =
                view.findViewById<View>(R.id.notificationWatchedStatusLinearLayout) as LinearLayout
            statusTextView =
                view.findViewById<View>(R.id.statusTextViewMyTeamNotificationRow) as TextView
            notificationSettings =
                view.findViewById<View>(R.id.myTeamNotificationSettingsImageButton) as ImageButton

            approveButton =
                view.findViewById<View>(R.id.approvalButtonMyTeamNotificationRow) as Button

            rejectButton =
                view.findViewById<View>(R.id.rejectButtonMyTeamNotificationRow) as Button

            settingPopup =
                view.findViewById<View>(R.id.settingPopupMyTeamNotificationItemRowLinearLayout) as LinearLayout

            clearNotificationSettingsInPopup =
                view.findViewById<View>(R.id.clearNotificationSettingPopupTextViewMyTeamNotificationItemRow) as TextView

            notificationSettings.setOnClickListener {
                if(settingPopup.isVisible){
                    settingPopup.visibility =View.GONE
                }else{
                    settingPopup.visibility =View.VISIBLE
                }
            }
            approveButton.setOnClickListener {
                approvedOnItemClick?.invoke(arrayList[adapterPosition])
            }

            rejectButton.setOnClickListener {
                rejectOnItemClick?.invoke(arrayList[adapterPosition])
            }

            clearNotificationSettingsInPopup.setOnClickListener {
                deleteNotificationSettingsOnItemClick?.invoke(arrayList[adapterPosition])
                settingPopup.visibility =View.GONE
            }
        }
    }

    fun clearData() {
        arrayList?.clear()
        notifyDataSetChanged()
    }

    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }
}