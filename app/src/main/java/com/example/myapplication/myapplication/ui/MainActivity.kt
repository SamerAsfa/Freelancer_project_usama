package com.example.myapplication.myapplication.ui

import android.R.attr.bitmap
import android.R.attr.data
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.myapplication.presentation.UserListViewModel
import com.example.myapplication.myapplication.utils.getDeviceId
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.upload_items_dialog.*
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    private val viewModel: UserListViewModel by viewModels()
    private lateinit var filePath: Uri
    private lateinit var  bitmap : Bitmap

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
                checkImage()
//                uploadImage(it)
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

    fun checkImage() {
        val image: InputImage;
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
        val detector = FaceDetection.getClient(realTimeOpts)

        try {
              image = InputImage.fromBitmap(bitmap, 0)

//            image = InputImage.fromFilePath(this, filePath)

            val result = detector.process(image)
                .addOnSuccessListener { faces ->
                    if(faces.size>0){
                        Toast.makeText(this@MainActivity, "Image", Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this@MainActivity, "not Image!!!!!", Toast.LENGTH_SHORT).show()
                    }
                    for (face in faces) {
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                        // nose available):
                        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR)
                        leftEar?.let {
                            val leftEarPos = leftEar.position
                        }

                        // If contour detection was enabled:
                        val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
                        val upperLipBottomContour = face.getContour(FaceContour.UPPER_LIP_BOTTOM)?.points

                        // If classification was enabled:
                        if (face.smilingProbability != null) {
                            val smileProb = face.smilingProbability
                        }
                        if (face.rightEyeOpenProbability != null) {
                            val rightEyeOpenProb = face.rightEyeOpenProbability
                        }

                        // If face tracking was enabled:
                        if (face.trackingId != null) {
                            val id = face.trackingId
                        }
                    }
                }
                .addOnFailureListener { e ->
                    print('d')
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun selectImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, 1)
        }
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(
//            Intent.createChooser(
//                intent,
//                "Select Image from here..."
//            ),
//            PICK_IMAGE_REQUEST
//        )
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
          bitmap  = intent?.extras?.get("data") as Bitmap

        print('d')
//        if (requestCode === REQUEST_IMAGE_CAPTURE && resultCode === RESULT_OK) {
//            val extras: Bundle = data.getExtras()
//            bitmap = extras["data"] as Bitmap?
//
//        }
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null && intent.data != null) {
//            filePath = intent.data!!
//        }
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

