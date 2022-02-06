package com.example.myapplication.myapplication.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.myapplication.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.upload_items_dialog.*

abstract class BaseActivity<VB : ViewBinding>(var bindingFactory: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }


    fun showBottomSheetDialog(onClick: (Boolean) -> Unit) {
        var state = false
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog)
        bottomSheetDialog.withImageRadioButton.setOnClickListener {
            state = true
        }
        bottomSheetDialog.withOutImageRadioButton.setOnClickListener {
            state = false
        }
        bottomSheetDialog.confirmImageView.setOnClickListener {
            bottomSheetDialog.dismiss()
            onClick(state)
        }
        bottomSheetDialog.show()
    }

    fun showAlertDialogCustomText(uploadImageView: () -> Unit,confirmButton: (String) -> Unit) {
        val dialogAndroidAppCus = Dialog(this)
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.upload_items_dialog)
        dialogAndroidAppCus.uploadImageView.setOnClickListener {
            uploadImageView()
        }
        dialogAndroidAppCus.confirmButton.setOnClickListener {
            dialogAndroidAppCus.dismiss()
            confirmButton(dialogAndroidAppCus.typeNameEditText.text.toString())
        }
        dialogAndroidAppCus.setCancelable(true)
        dialogAndroidAppCus.show()
    }

}