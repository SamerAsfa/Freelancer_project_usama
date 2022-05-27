package com.example.myapplication.myapplication.base

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.models.ActionModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.dialog_bunch_msg.*
import kotlinx.android.synthetic.main.dialog_bunch_msg.stateNameTextView
import kotlinx.android.synthetic.main.dialog_msg.bodyNameEditText
import kotlinx.android.synthetic.main.dialog_msg.cancelButton
import kotlinx.android.synthetic.main.dialog_msg.okButton
import kotlinx.android.synthetic.main.dialog_success.*
import kotlinx.android.synthetic.main.upload_items_dialog.*

open class BaseFragment : Fragment() {
    var dialog: ProgressDialog? = null

    fun showBottomSheetDialog(onClick: (Boolean) -> Unit) {
        var state = false
        val bottomSheetDialog = BottomSheetDialog(requireContext())
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
        val dialogAndroidAppCus = Dialog(requireContext())
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


    fun showSuccessDialog() {
        val dialogAndroidAppCus = Dialog(requireContext())
        dialogAndroidAppCus.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.dialog_success)
        dialogAndroidAppCus.publicDialog.setOnClickListener {
            dialogAndroidAppCus.dismiss()
        }
        dialogAndroidAppCus.setCancelable(true)
        dialogAndroidAppCus.show()
    }


    fun showDialogTowButtonsStateBunch(
        title: String,
        body: String,
        okButtonText: String,
        canceButtonText: String,
        actionModel: ActionModel,
        ok: (Dialog,ActionModel) -> Unit,
        cancel: (Dialog) -> Unit
    ) {
        val dialogAndroidAppCus = Dialog(requireContext())
        dialogAndroidAppCus.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.dialog_bunch_msg)
        dialogAndroidAppCus.stateNameTextView.text = title
        dialogAndroidAppCus.okButton.text = okButtonText
        dialogAndroidAppCus.cancelButton.text = canceButtonText
        dialogAndroidAppCus.bodyNameEditText.text = body
        dialogAndroidAppCus.okButton.setOnClickListener {
            ok.invoke(dialogAndroidAppCus,actionModel)
        }
        dialogAndroidAppCus.cancelButton.setOnClickListener {
            cancel.invoke(dialogAndroidAppCus)
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
        val dialogAndroidAppCus = Dialog(requireContext())
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.dialog_msg)
//        dialogAndroidAppCus.userNameTextView.text = title
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
        val dialogAndroidAppCus = Dialog(requireContext())
        dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAndroidAppCus.setContentView(R.layout.dialog_msg)
//        dialogAndroidAppCus.userNameTextView.text = title
        dialogAndroidAppCus.okButton.text = okButtonText
        dialogAndroidAppCus.cancelButton.visibility = View.GONE
        dialogAndroidAppCus.bodyNameEditText.text = body
        dialogAndroidAppCus.okButton.setOnClickListener {
            ok.invoke(dialogAndroidAppCus)
        }
        dialogAndroidAppCus.setCancelable(true)
        dialogAndroidAppCus.show()
    }


    fun toggleProgressDialog(show: Boolean, activity: Activity, msg : String) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", msg, true);
            } else {
                dialog?.dismiss();
            }
        }
    }





}