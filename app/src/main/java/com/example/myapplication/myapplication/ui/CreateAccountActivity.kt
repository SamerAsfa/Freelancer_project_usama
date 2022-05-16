package com.example.myapplication.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest.Companion.registerApi
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.UserModel
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity(), ResponseApi {
    var userModel: UserModel? = null
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM = 0x01 shl 9
    fun String.equalsIgnoreCase(other: String) =
        (this as java.lang.String).equalsIgnoreCase(other)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        custom_btn.setOnClickListener {
            if (emailEditText.text.toString().equalsIgnoreCase(reEmailEditText.text.toString())) {
                if (passwordEditText.text.toString()
                        .equalsIgnoreCase(retypePasswordEditText.text.toString())
                ) {
                    var maps: MutableMap<String, String> = HashMap()
                    maps.put("name", nameEditText.text.toString())
                    maps.put("email", emailEditText.text.toString())
                    maps.put("password", passwordEditText.text.toString())
                    maps.put("code", organizationEditText.text.toString())
                    maps.put("mobile_number", numberPhoneEditText.text.toString())
                    maps.put("fcm", LongTermManager.getInstance().notificationsToken)
                    if (userModel == null) {
                        POSTMediasTask().post(
                            this@CreateAccountActivity,
                            registerApi,
                            maps,
                            this@CreateAccountActivity
                        )
                    } else {
                        startCustomActivity(userModel!!)
                    }
                } else {
                    Toast.makeText(
                        this, "Password is mismatch",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this, "Email is mismatch",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onSuccessCall(response: String?) {
        response?.let {
            this.userModel = UserModel().parse(it)
//            LongTermManager.getInstance().userModel = userModel
            if (ActivityCompat.checkSelfPermission(
                    this@CreateAccountActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                userModel?.let { it1 -> startCustomActivity(it1) }
            }
            ActivityCompat.requestPermissions(
                this@CreateAccountActivity,
                PERMISSIONS,
                RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM,
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE_CUSTOM && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            userModel?.let { startCustomActivity(it) }
        }
    }

    private fun startCustomActivity(userModel: UserModel) {
        val intent = Intent(this, CustomWithVideo::class.java)
        intent.putExtra("user", userModel)
        this.startActivityForResult(intent, 44)
    }

    override fun onErrorCall(error: VolleyError?) {

    }


}