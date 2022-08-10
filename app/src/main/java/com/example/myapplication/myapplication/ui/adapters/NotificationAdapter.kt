package com.example.myapplication.myapplication.ui.adapters


import android.content.Context
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.common.UserNoteType
import com.example.myapplication.myapplication.models.NotificationModel

class NotificationAdapter(
    private val arrayList: java.util.ArrayList<NotificationModel>?,
    private val context:Context
) :
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
        val position = holder.adapterPosition


        holder.dateTextView.text =
            arrayList?.get(position)?.updated_at?.indexOf('T')
                ?.let { lastPositionToSubString ->
                    arrayList[position].updated_at?.substring(0 , lastPositionToSubString)
                }


        /* holder.dateTextView.text = String.format(
             Locale.CANADA,
             "%sth %s",
             DateUtils().TMonthformatUtc(arrayList?.get(holder.adapterPosition)?.created_at),
             DateUtils().TTimeformatUtc(arrayList?.get(holder.adapterPosition)?.created_at)
         )*/

        when(arrayList?.get(holder.adapterPosition)?.not_type){

            UserNoteType.PUNCH ->{}

            UserNoteType.LEAVE_REQUEST ->{

                holder.msgTextView.text = "Applied for Leave Request."

                holder.stateTextView.apply {
                    text =context.getString(R.string.approval_pending)
                    setTextColor( Color.parseColor("#FFFFFF"))
                    textSize =8f
                    background =context.getDrawable(R.drawable.approved_pending)
                }
            }

            UserNoteType.LEAVE_APPROVED ->{

                holder.msgTextView.text = " HR Approved Your Registration Request."

                holder.stateTextView.apply {
                    text =context.getString(R.string.approved)
                    setTextColor( Color.parseColor("#00D865"))
                    textSize =14f
                    background =null
                }
            }

            UserNoteType.LEAVE_REJECT ->{

                holder.msgTextView.text = "Applied for Leave Request"

                holder.stateTextView.apply {
                    text =context.getString(R.string.rejected)
                    setTextColor( Color.parseColor("#FF0000"))
                    textSize =14f
                    background =null
                }
            }

            UserNoteType.REVERIFY_ACCOUNT ->{

                holder.msgTextView.text = "Your Registration Request needs Re-Verify."

                holder.stateTextView.apply {
                    text =context.getString(R.string.approved)
                    setTextColor( Color.parseColor("#FFFFFF"))
                    textSize =8f
                    background =context.getDrawable(R.drawable.re_verify)
                }
            }

            UserNoteType.APPROVED_ACCOUNT ->{}

            UserNoteType.DISABLED_ACCOUNT ->{

                val disabled = getColoredSpanned("Disabled", "#EC1119")

                holder.msgTextView.setText(Html.fromHtml("Your Account has been Disabled "+" "+disabled +" by HR manager."))
               // holder.msgTextView.text = context.getString(R.string.account_disabled)//"Your Account has been Disabled by HR manager."
                holder.stateTextView.visibility =View.INVISIBLE
            }

        }


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

    fun clearData(){
        arrayList?.clear()
        notifyDataSetChanged()
    }
    private fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }
}