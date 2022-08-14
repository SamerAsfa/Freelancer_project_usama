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
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.common.UserNoteType
import com.example.myapplication.myapplication.models.NotificationModel

class MyTeamNotificationAdapter(
    private val arrayList: java.util.ArrayList<NotificationModel>?,
    private val context: Context
) :
    RecyclerView.Adapter<MyTeamNotificationAdapter.ViewHolder>() {


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
        var notificationModel: NotificationModel? = arrayList?.get(position)

        holder.nameTextView.text =notificationModel?.body.toString()

        holder.dateTextView.text =
            arrayList?.get(position)?.updated_at?.indexOf('T')
                ?.let { lastPositionToSubString ->
                    arrayList[position].updated_at?.substring(0 , lastPositionToSubString)
                }

        holder.descriptionTextView.text = notificationModel?.body


        /* holder.dateTextView.text = String.format(
             Locale.CANADA,
             "%sth %s",
             DateUtils().TMonthformatUtc(arrayList?.get(holder.adapterPosition)?.created_at),
             DateUtils().TTimeformatUtc(arrayList?.get(holder.adapterPosition)?.created_at)
         )*/

       when(notificationModel?.not_type){

            UserNoteType.PUNCH ->{
                holder.statusLinearLayout.visibility =View.INVISIBLE
                holder.watchedStatusLinearLayout.visibility =View.GONE
            }

            UserNoteType.LEAVE_REQUEST ->{

              holder.statusLinearLayout.visibility =View.VISIBLE
              holder.watchedStatusLinearLayout.visibility =View.GONE
            }

            UserNoteType.LEAVE_APPROVED ->{

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

                holder.statusLinearLayout.visibility =View.GONE
                holder.watchedStatusLinearLayout.visibility =View.VISIBLE

                holder.statusTextView.apply {
                    text =context.getString(R.string.rejected)
                    setTextColor( Color.parseColor("#FF0000"))
                    textSize =14f
                    background =null
                }
            }

            UserNoteType.REVERIFY_ACCOUNT ->{

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

/*        val userModel = LongTermManager.getInstance().userModel
        if (!userModel?.profile_photo_path?.trim()?.isBlank()!!) {
//            Glide.with()
//                .load(userModel?.profile_photo_path).into(view.userImage)
        }*/
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userImage: ImageView
        var nameTextView: TextView
        var descriptionTextView: TextView
        var dateTextView: TextView
        var statusLinearLayout: LinearLayout
        var watchedStatusLinearLayout: LinearLayout
        var statusTextView: TextView
        var notificationSettings: ImageButton

        init {
            userImage = view.findViewById<View>(R.id.imageViewMyTeamNotificationRow) as ImageView
            nameTextView =
                view.findViewById<View>(R.id.employeeNameTextViewMyTeamNotificationRow) as TextView
            descriptionTextView =
                view.findViewById<View>(R.id.statusDescriptionTextViewMyTeamNotificationRow) as TextView
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