package com.example.myapplication.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.myapplication.R
import kotlinx.android.synthetic.main.fragment_history.*
import org.w3c.dom.Text

class HistoryFragment : Fragment(R.layout.fragment_history) {
    companion object{
        fun getInstance() = HistoryFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.findViewById<TextView>(R.id.textM)?.text = "usa,a"


        return super.onCreateView(inflater, container, savedInstanceState)
    }



}