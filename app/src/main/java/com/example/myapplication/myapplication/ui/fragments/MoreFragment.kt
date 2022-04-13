package com.example.myapplication.myapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.myapplication.R

class MoreFragment : Fragment(R.layout.fragment_more) {


    companion object {
        val fragmentName : String = "MoreFragment"

        val moreFragment: MoreFragment? = null
        fun getInstance(): MoreFragment {
            return moreFragment ?: MoreFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("")
    }




}