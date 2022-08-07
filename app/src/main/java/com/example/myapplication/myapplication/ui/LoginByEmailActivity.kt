package com.example.myapplication.myapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.data.APIInterface
import com.example.myapplication.myapplication.data.remote.dto.LoginByEmailBody
import com.example.myapplication.myapplication.models.UserModel
import com.example.myapplication.myapplication.ui.fragments.NotificationsFragment
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_login_by_email.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginByEmailActivity : AppCompatActivity() {

    companion object {
        //  val FACE_BUNDLE = "faceBundle"
        //  val RESULT = "result"
        /*     fun startActivity(context: Context): Intent {
                 val intent = Intent(context, LoginByEmailActivity::class.java)
                 //intent.putExtra(FACE_BUNDLE, faceBundle)
                 return intent
             }*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_by_email)

        initUiListener()
    }


    private fun initUiListener() {
        createNewNowTextView.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        loginByEmailButton.setOnClickListener {

            val email: String = organizationOrEmailEditText.text.toString()
            val password: String = passwordLoginByEmailFragment.text.toString()

            if (email.isNullOrEmpty()) {
                organizationOrEmailEditText.error = getString(R.string.field_required)
                organizationOrEmailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isNullOrEmpty()) {
                passwordTextInputLayoutLoginByEmailFragment.error =
                    getString(R.string.field_required)
                passwordTextInputLayoutLoginByEmailFragment.requestFocus()
                return@setOnClickListener
            }

            loginByEmail(email, password)
        }
    }


    private fun loginByEmail(email: String, password: String) {
        try {

            val url = "http://frapi.hr-jo.com/api/"

            val builder = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())

            val retrofit = builder.build()



            val body:LoginByEmailBody = LoginByEmailBody(
                email="teamLead@email.com",
                password ="123456",
                device_id="123456",
                fcm ="123456",
                lang="en"
            )
            val client: APIInterface = retrofit.create(APIInterface::class.java)
           /* val call: Call<UserModel?>? = client.loginByEmail(
                email="teamLead@email.com",
                password ="123456",
                device_id=123456,
                fcm =123456,
                lang="en")*/

            val call: Call<UserModel?>? = client.loginByEmail(body)
            call?.enqueue(object : Callback<UserModel?> {

                override fun onResponse(
                    call: Call<UserModel?>,
                    response: Response<UserModel?>
                ) {
                    val result: UserModel? = response.body()
                    if (response.isSuccessful) {
                        LongTermManager.getInstance().userModel = result // userModel
                        NavigationActivity().clearAndStart(this@LoginByEmailActivity)
                    } else {
                        Toast.makeText(
                            this@LoginByEmailActivity,
                            "Email or password wrong... Pleas try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserModel?>, t: Throwable) {
                    Toast.makeText(
                        this@LoginByEmailActivity,
                        "Email or password wrong... Pleas try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        } catch (ex: Exception) {
            ex.message
        }
    }


    private fun swapFragmentsWithAnimation(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            if (supportFragmentManager.fragments.size == 0) {
                add(R.id.flFragment, fragment, tag)
            } else {
                var isCalled = false
                var calledFragment: Fragment? = null
                for (index in 0 until supportFragmentManager.fragments.size) {
                    if (supportFragmentManager.fragments[index].javaClass.name == fragment.javaClass.name) {
                        calledFragment = supportFragmentManager.fragments[index]
                        isCalled = true
                        break
                    }
                }
                if (isCalled) {
                    calledFragment?.let { replace(R.id.flFragment, it, tag) }
                } else {
                    add(R.id.flFragment, fragment, tag)
                }
            }
            commit()
        }
}