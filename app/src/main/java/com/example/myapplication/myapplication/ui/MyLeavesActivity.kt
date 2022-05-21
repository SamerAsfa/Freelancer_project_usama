package com.example.myapplication.myapplication.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseActivity
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.ui.adapters.LeavesAdapter
import kotlinx.android.synthetic.main.activity_my_leaves.*
import java.util.*

class MyLeavesActivity : BaseActivity() {
    var currentIndex: Int = 0


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
        POSTMediasTask().get(
            this,
            BaseRequest.leaveApi,
            "/${monthYear}",
            object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    val arrayHistory: ArrayList<HistoryModel?>? = response?.let {
                        HistoryModel().parseArray(
                            it
                        )
                    }
                    val scanMainAdapter = LeavesAdapter(arrayHistory)
                    historyRecyclerView?.setHasFixedSize(true)
                    historyRecyclerView?.layoutManager = LinearLayoutManager(this@MyLeavesActivity)
                    historyRecyclerView?.adapter = scanMainAdapter
                }

                override fun onErrorCall(error: VolleyError?) {
                    print("")
                }
            }
        )
    }

}