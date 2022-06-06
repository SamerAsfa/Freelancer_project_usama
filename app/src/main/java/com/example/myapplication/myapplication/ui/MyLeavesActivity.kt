package com.example.myapplication.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.*
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.ui.adapters.LeavesAdapter
import kotlinx.android.synthetic.main.activity_my_leaves.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.util.*

class MyLeavesActivity : BaseActivity() {
    var currentIndex: Int = 0
    var apiInterface: APIInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_leaves)
        leaveRequestButton.setOnClickListener {
            val intent = Intent(this, RequstLeaveActivity::class.java)
            startActivity(intent)
        }

        vectorRightImageView.setOnClickListener {
            increaseMonth()
        }
        vectorLeftImageView.setOnClickListener {
            decreaseMonth()
        }
        apiInterface = APIClient.client?.create(APIInterface::class.java)
    }

    override fun onResume() {
        super.onResume()
        initCurrentDate(Date().time)
    }

    private fun increaseMonth() {
        currentIndex++
        initCurrentDate(DateUtils().addOneMonth(currentIndex).time)
    }

    private fun decreaseMonth() {
        currentIndex--
        initCurrentDate(DateUtils().addOneMonth(currentIndex).time)
    }

    private fun initCurrentDate(timeStamp: Long) {
        dateTextView?.text = DateUtils().getDateFromTimeStamp(timeStamp)
        val monthYear = DateUtils().getDateForApiFromTimeStamp(timeStamp)
        getMonth(monthYear)
    }

    private fun getMonth(monthYear: String) {
        try {
            val call = apiInterface?.getLeaveHistoryApi(monthYear)
            call?.enqueue(object : retrofit2.Callback<ArrayList<HistoryModel>?> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<HistoryModel>?>,
                    response: retrofit2.Response<ArrayList<HistoryModel>?>
                ) {
                    val arrayHistory = response.body()
                    val scanMainAdapter = LeavesAdapter(arrayHistory)
                    historyRecyclerView?.setHasFixedSize(true)
                    historyRecyclerView?.layoutManager = LinearLayoutManager(this@MyLeavesActivity)
                    historyRecyclerView?.adapter = scanMainAdapter
                }

                override fun onFailure(
                    call: retrofit2.Call<ArrayList<HistoryModel>?>,
                    t: Throwable
                ) {
                    call.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }

}