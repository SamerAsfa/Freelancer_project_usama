package com.example.myapplication.myapplication.ui.auth.create_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.example.myapplication.myapplication.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.example.myapplication.myapplication.databinding.FragmentCreateAccountBinding


@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    private val viewModels: CreateAccountViewModel by viewModels()
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)

        uiListener()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uiListener(){
        binding.organizationEditText.doOnTextChanged { text, start, before, count ->

            if(count >= 4){
                TODO("call api to check if code correct or not")
            }
        }
    }
}