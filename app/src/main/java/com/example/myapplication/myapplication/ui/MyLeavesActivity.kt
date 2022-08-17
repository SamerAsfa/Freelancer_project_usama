package com.example.myapplication.myapplication.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.data.*
import com.example.myapplication.myapplication.data.APIClient
import com.example.myapplication.myapplication.models.MyLeaveHistoryModel
import com.example.myapplication.myapplication.models.MyTeamLeaveHistoryModel
import com.example.myapplication.myapplication.ui.adapters.MyLeavesHistoryAdapter
import com.example.myapplication.myapplication.ui.adapters.MyTeamLeavesHistoryAdapter
import kotlinx.android.synthetic.main.activity_my_leaves.*
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

        initUIListener()
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

            getMyLeaveHistoryByMonth(monthYear)
            getMyTeamLeaveHistoryByMonth(monthYear)
    }

    private fun getMyLeaveHistoryByMonth(monthYear: String) {
        try {
            val call = apiInterface?.getLeaveHistoryApi(monthYear)
            call?.enqueue(object : retrofit2.Callback<ArrayList<MyLeaveHistoryModel>?> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<MyLeaveHistoryModel>?>,
                    response: retrofit2.Response<ArrayList<MyLeaveHistoryModel>?>
                ) {
                    val arrayHistory = response.body()
                    val scanMainAdapter = MyLeavesHistoryAdapter(arrayHistory)
                    myLeaveHistoryRecyclerView?.setHasFixedSize(true)
                    myLeaveHistoryRecyclerView?.layoutManager = LinearLayoutManager(this@MyLeavesActivity)
                    myLeaveHistoryRecyclerView?.adapter = scanMainAdapter
                }

                override fun onFailure(
                    call: retrofit2.Call<ArrayList<MyLeaveHistoryModel>?>,
                    t: Throwable
                ) {
                    call.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }

    private fun getMyTeamLeaveHistoryByMonth(monthYear: String) {
        try {
            val call = apiInterface?.getMyTeamLeaveHistoryApi(monthYear)
            call?.enqueue(object : retrofit2.Callback<ArrayList<MyTeamLeaveHistoryModel>?> {
                override fun onResponse(
                    call: retrofit2.Call<ArrayList<MyTeamLeaveHistoryModel>?>,
                    response: retrofit2.Response<ArrayList<MyTeamLeaveHistoryModel>?>
                ) {
                    val arrayHistory = response.body()
                    val myTeamLeaveHistoryAdapter = MyTeamLeavesHistoryAdapter(arrayHistory)
                    myTeamLeaveHistoryRecyclerView?.setHasFixedSize(true)
                    myTeamLeaveHistoryRecyclerView?.layoutManager = LinearLayoutManager(this@MyLeavesActivity)
                    myTeamLeaveHistoryRecyclerView?.adapter = myTeamLeaveHistoryAdapter
                }

                override fun onFailure(
                    call: retrofit2.Call<ArrayList<MyTeamLeaveHistoryModel>?>,
                    t: Throwable
                ) {
                    call.cancel()
                }
            })
        } catch (ex: Exception) {
            ex.message
        }
    }


    private fun initUIListener(){

      myLeavesTextView.setOnClickListener {

          myLeavesTextView.setTextColor(Color.parseColor("#000000"))
          myLeavesUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

          myTeamLeavesTextView.setTextColor(Color.parseColor("#708792"))
          myTeamLeavesUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

          myLeaveHistoryConstraintLayout.visibility = View.VISIBLE
          myTeamLeaveHistoryConstraintLayout.visibility = View.GONE
        }


        myTeamLeavesTextView.setOnClickListener {

            myTeamLeavesTextView.setTextColor(Color.parseColor("#000000"))
            myTeamLeavesUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            myLeavesTextView.setTextColor(Color.parseColor("#708792"))
            myLeavesUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            myLeaveHistoryConstraintLayout.visibility = View.GONE
            myTeamLeaveHistoryConstraintLayout.visibility = View.VISIBLE
        }

    }

}