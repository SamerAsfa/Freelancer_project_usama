package com.example.myapplication.myapplication.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.models.LanguageModel

class CustomDropDownAdapter(val context: Context,private val dataSource: ArrayList<LanguageModel>, private val clicks: LanguageAdapter.Clicks) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.language_item, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = dataSource.get(position).name
        dataSource.get(position).flag?.let { vh.img.setBackgroundResource(it) }
        return view
    }


    interface Clicks{
        fun click(typesModel: LanguageModel, position: Int)
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val label: TextView
        val img: ImageView

        init {
            label = row?.findViewById(R.id.titleTextView) as TextView
            img = row.findViewById(R.id.flagImageView) as ImageView
        }
    }

}