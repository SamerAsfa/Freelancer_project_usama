package com.example.myapplication.myapplication.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.HiltApplication
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.LongTermManager
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.ui.fragments.HistoryFragment
import com.example.myapplication.myapplication.ui.fragments.HomeFragment
import com.example.myapplication.myapplication.ui.fragments.MoreFragment
import com.example.myapplication.myapplication.ui.fragments.NotificationsFragment
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_navigation.*


class NavigationActivity : BaseActivity() {


    fun clearAndStart(context: Context) {
        val intent = Intent(context, NavigationActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        (context as Activity).overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        HiltApplication.mContext = this
        updateToken()
    }


    fun getFcm(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            LongTermManager.getInstance().notificationsToken = token
        })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        getFcm()
        val homeFragment = HomeFragment.getInstance()
        val historyFragment = HistoryFragment.getInstance()
        val notificationsFragment = NotificationsFragment.getInstance()
        val moreFragment = MoreFragment.getInstance()


        swapFragmentsWithAnimation(homeFragment, HomeFragment.fragmentName)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> swapFragmentsWithAnimation(homeFragment, HomeFragment.fragmentName)
                R.id.history -> swapFragmentsWithAnimation(
                    historyFragment,
                    HistoryFragment.fragmentName
                )
                R.id.notification -> swapFragmentsWithAnimation(
                    notificationsFragment,
                    NotificationsFragment.fragmentName
                )
                R.id.more -> swapFragmentsWithAnimation(moreFragment, MoreFragment.fragmentName)
            }
            true
        }
    }

    fun updateToken(){
        var maps: MutableMap<String, String> = HashMap()
        maps.put("fcm", LongTermManager.getInstance().notificationsToken)
        toggleProgressDialog(show = true,this,this.resources.getString(R.string.loading))
        POSTMediasTask().postWithHeader(
                this@NavigationActivity,
                BaseRequest.updateTokenFcmApi,
                maps,
                responseApi = object : ResponseApi{
                    override fun onSuccessCall(response: String?) {
                        toggleProgressDialog(show = false,this@NavigationActivity,this@NavigationActivity.resources.getString(R.string.loading))
                        print("")
                    }

                    override fun onErrorCall(error: VolleyError?) {
                        toggleProgressDialog(show = false,this@NavigationActivity,this@NavigationActivity.resources.getString(R.string.loading))
                        print("")
                    }
                }
            )
    }

    private fun swapFragmentsWithAnimation(fragment: Fragment,tag : String) =
        supportFragmentManager.beginTransaction().apply {
            if(supportFragmentManager.fragments.size==0){
                add(R.id.flFragment, fragment,tag)
            }else{
                var isCalled = false
                var calledFragment: Fragment? = null
                for (index in 0 until supportFragmentManager.fragments.size) {
                    if(supportFragmentManager.fragments[index].javaClass.name==fragment.javaClass.name){
                        calledFragment = supportFragmentManager.fragments[index]
                        isCalled = true
                        break
                    }
                }
                if(isCalled){
                    calledFragment?.let { replace(R.id.flFragment, it,tag) }
                }else{
                    add(R.id.flFragment, fragment,tag)
                }
            }
            commit()
        }



}