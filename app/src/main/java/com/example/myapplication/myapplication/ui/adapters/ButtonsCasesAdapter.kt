package com.example.myapplication.myapplication.ui.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.AppUtils.getTextRes
import com.example.myapplication.myapplication.data.PublicKeys
import com.example.myapplication.myapplication.models.ActionModel
import com.example.myapplication.myapplication.ui.fragments.HomeFragment


class ButtonsCasesAdapter(
    private val arrayList: ArrayList<ActionModel>?,
    private val context: Context?,
    private val onItemClick: (ActionModel?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (HomeFragment.ButtonState.PUNCH_IN.ordinal == viewType) {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.adaoter_buttons_cases, parent, false)
            return ViewHolderIsLargeView(v)
        } else {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_button_casess_small, parent, false)
            return ViewHolderIsDefaultView(v)
        }
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder.itemViewType == ActionModel.ViewState.LARGE.ordinal) {
            val viewHolderPunchIn: ViewHolderIsLargeView = holder as ViewHolderIsLargeView
            val actionModel = arrayList?.get(holder.adapterPosition)
            val state = arrayList?.get(holder.adapterPosition)?.key
            val disable = !arrayList?.get(holder.adapterPosition)?.disable!!
            if (state == PublicKeys.PIN) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_punch_in_big_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.POUT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_punch_out_big_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.BREAK_IT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_break_big_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.BREAK_OUT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_break_big_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.LEAVE_IN) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_leave_big_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.LEAVE_OUT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_leave_big_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else {
                viewHolderPunchIn.scerculerImageView.visibility = View.GONE
                viewHolderPunchIn.textStateTextView.visibility = View.GONE
            }

            viewHolderPunchIn.scerculerImageView.setOnClickListener {
                onItemClick.invoke(actionModel)
            }

        } else {
            val viewHolderPunchIn: ViewHolderIsDefaultView = holder as ViewHolderIsDefaultView
            val actionModel = arrayList?.get(holder.adapterPosition)
            val state = arrayList?.get(holder.adapterPosition)?.key
            val disable = !arrayList?.get(holder.adapterPosition)?.disable!!
            if (state == PublicKeys.PIN) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_punch_in_small_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.POUT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_punch_out_small_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.BREAK_IT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_break_small_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.BREAK_OUT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_break_small_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.LEAVE_IN) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_leave_small_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else if (state == PublicKeys.LEAVE_OUT) {
                if (disable == true) {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_leave_small_button)
                    viewHolderPunchIn.scerculerImageView.isEnabled = true
                } else {
                    viewHolderPunchIn.scerculerImageView.setBackgroundResource(R.drawable.ic_ellipse)
                    viewHolderPunchIn.scerculerImageView.isEnabled = false
                }
                viewHolderPunchIn.textStateTextView.text =
                    context?.resources?.getText(getTextRes(state))
                viewHolderPunchIn.scerculerImageView.visibility = View.VISIBLE
                viewHolderPunchIn.textStateTextView.visibility = View.VISIBLE
            } else {
                viewHolderPunchIn.scerculerImageView.visibility = View.GONE
                viewHolderPunchIn.textStateTextView.visibility = View.GONE
            }
            viewHolderPunchIn.scerculerImageView.setOnClickListener {
                onItemClick.invoke(actionModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arrayList?.get(position)?.isLargeView == true) {
            ActionModel.ViewState.LARGE.ordinal
        } else
            ActionModel.ViewState.DEFAULT.ordinal
    }




//    private fun getState(state: String): Int {
//        if (state == PublicKeys.PIN) {
//            return HomeFragment.ButtonState.PUNCH_IN.ordinal
//        } else if (state == PublicKeys.POUT) {
//            return HomeFragment.ButtonState.PUNCH_OUT.ordinal
//        } else if (state == PublicKeys.BREAK_IT) {
//            return HomeFragment.ButtonState.BREAK_IN.ordinal
//        } else if (state == PublicKeys.BREAK_OUT) {
//            return HomeFragment.ButtonState.BREAK_OUT.ordinal
//        } else if (state == PublicKeys.LEAVE_IN) {
//            return HomeFragment.ButtonState.LEAVE_IN.ordinal
//        } else {
//            return HomeFragment.ButtonState.LEAVE_OUT.ordinal
//        }
//    }


    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    class ViewHolderIsLargeView(view: View) : RecyclerView.ViewHolder(view) {
        var scerculerImageView: ImageView =
            view.findViewById<View>(R.id.scerculerImageView) as ImageView
        var textStateTextView: TextView =
            view.findViewById<View>(R.id.textStateTextView) as TextView
    }

    class ViewHolderIsDefaultView(view: View) : RecyclerView.ViewHolder(view) {
        var scerculerImageView: ImageView =
            view.findViewById<View>(R.id.scerculerImageView) as ImageView
        var textStateTextView: TextView =
            view.findViewById<View>(R.id.textStateTextView) as TextView
    }


}