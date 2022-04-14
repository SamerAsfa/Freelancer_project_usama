package com.example.myapplication.myapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.CustomWithVideo
import com.example.myapplication.myapplication.ui.MyLeavesActivity
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.userImage
import kotlinx.android.synthetic.main.fragment_more.view.*
import java.util.*

class MoreFragment : Fragment(R.layout.fragment_more) {

    protected var mainView: View? = null
    var userModel: UserModel? = null

    companion object {
        val fragmentName: String = "MoreFragment"

        val moreFragment: MoreFragment? = null
        fun getInstance(): MoreFragment {
            return moreFragment ?: MoreFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = LongTermManager.getInstance().userModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_more, container, false)
        mainView = view
        if (!userModel?.profile_photo_path?.trim()?.isBlank()!!) {
            Glide.with(view.context)
                .load(userModel?.profile_photo_path).into(view.userImage)

        }

        view.profileTextView.setOnClickListener {

        }

        view.myLeavesTextView.setOnClickListener {
            val intent = Intent(requireContext(), MyLeavesActivity::class.java)
            startActivity(intent)
        }

        view.settingsTextView.setOnClickListener {

        }

        view.logOutTextView.setOnClickListener {

        }

        return view
    }


}