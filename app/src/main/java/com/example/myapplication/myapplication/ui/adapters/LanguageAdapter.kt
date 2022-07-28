package com.example.myapplication.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.models.LanguageModel
import com.example.myapplication.myapplication.models.TypesModel

class LanguageAdapter(private val arrayList: ArrayList<LanguageModel>, private val clicks: Clicks) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return ViewHolder(v)
    }


    interface Clicks{
        fun click(typesModel: LanguageModel, position: Int)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.titleTextView.text =
            arrayList?.get(holder.adapterPosition)?.name
        holder.titleTextView.setOnClickListener {
            arrayList?.get(holder.adapterPosition)
                ?.let { it1 -> clicks.click(it1,holder.adapterPosition) }
        }
    }

    override fun getItemCount(): Int {
        return arrayList?.size!!
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleTextView: TextView

        init {
            titleTextView = view.findViewById<View>(R.id.titleTextView) as TextView
        }
    }

}