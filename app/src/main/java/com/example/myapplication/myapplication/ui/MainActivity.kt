package com.example.myapplication.myapplication.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.myapplication.presentation.UserListViewModel
import com.example.myapplication.myapplication.utils.getDeviceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.upload_items_dialog.*
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    private val viewModel: UserListViewModel by viewModels()
    private lateinit var filePath: Uri


    companion object {
        private const val PICK_IMAGE_REQUEST = 22
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupListeners()
    }


    private fun setupListeners() {
        addDataImageView.setOnClickListener {
            showAlertDialogCustomText(uploadImageView = { selectImage() }, confirmButton = {
                uploadImage(it)
            })
        }
        filterImageView.setOnClickListener {
            showBottomSheetDialog {
                viewModel.filterImagesData(it)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchByNameData(newText)
                return false
            }
        })
    }


    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            intent
        )
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null && intent.data != null) {
            filePath = intent.data!!
        }
    }

    private fun uploadImage(name: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
        viewModel.uploadingImage(filePath, addOnSuccessListener = {
        }, addOnCompleteListener = {
            try {
                progressDialog.dismiss()
                viewModel.addUserData(
                    name = name,
                    url = it.toString(),
                    deviceId = getDeviceId()
                )
                Toast.makeText(this@MainActivity, "Uploaded", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.toString()
            }
        }, addOnFailureListener = {
            progressDialog.dismiss()
            Toast.makeText(this@MainActivity, "Error to Upload Image", Toast.LENGTH_SHORT)
                .show()
        })
    }


}

