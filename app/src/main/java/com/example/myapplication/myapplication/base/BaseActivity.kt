package com.example.myapplication.myapplication.base

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.myapplication.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.dialog_msg.*
import kotlinx.android.synthetic.main.upload_items_dialog.*
import kotlinx.android.synthetic.main.upload_items_dialog.typeNameEditText
import kotlinx.android.synthetic.main.upload_items_dialog.userNameTextView

abstract class BaseActivity :
    AppCompatActivity() {
    var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    fun showAlertDialogCustomText(uploadImageView: () -> Unit, confirmButton: (String) -> Unit) {
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


    fun showDialogTowButtonsCustom(
        title: String,
        body: String,
        okButtonText: String,
        canceButtonText: String,
        ok: (Dialog) -> Unit,
        cancel: (Dialog) -> Unit
    ) {
        val dialogAndroidAppCus = Dialog(this)
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.dialog_msg)
        dialogAndroidAppCus.userNameTextView.text = title
        dialogAndroidAppCus.okButton.text = okButtonText
        dialogAndroidAppCus.cancelButton.text = canceButtonText
        dialogAndroidAppCus.bodyNameEditText.text = body
        dialogAndroidAppCus.okButton.setOnClickListener {
            ok.invoke(dialogAndroidAppCus)
        }
        dialogAndroidAppCus.cancelButton.setOnClickListener {
            cancel.invoke(dialogAndroidAppCus)
        }
        dialogAndroidAppCus.setCancelable(true)
        dialogAndroidAppCus.show()
    }

    fun showDialogOneButtonsCustom(
        title: String,
        body: String,
        okButtonText: String,
        ok: (Dialog) -> Unit,
    ) {
        val dialogAndroidAppCus = Dialog(this)
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.dialog_msg)
        dialogAndroidAppCus.userNameTextView.text = title
        dialogAndroidAppCus.okButton.text = okButtonText
        dialogAndroidAppCus.cancelButton.visibility = View.GONE
        dialogAndroidAppCus.bodyNameEditText.text = body
        dialogAndroidAppCus.okButton.setOnClickListener {
            ok.invoke(dialogAndroidAppCus)
        }
        dialogAndroidAppCus.setCancelable(true)
        dialogAndroidAppCus.show()
    }


    fun toggleProgressDialog(show: Boolean, activity: Activity,msg : String) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", msg, true);
            } else {
                dialog?.dismiss();
            }
        }
    }


}