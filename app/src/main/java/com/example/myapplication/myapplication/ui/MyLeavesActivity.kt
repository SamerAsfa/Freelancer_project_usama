package com.example.myapplication.myapplication.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
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

    var myTeamLeaveHistoryAdapter: MyTeamLeavesHistoryAdapter =
        MyTeamLeavesHistoryAdapter(this@MyLeavesActivity)
    var myLeaveHistoryAdapter: MyLeavesHistoryAdapter = MyLeavesHistoryAdapter()


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
                    arrayHistory?.let { myLeaveHistoryAdapter.submitData(it) }
                    myLeaveHistoryRecyclerView?.setHasFixedSize(true)
                    myLeaveHistoryRecyclerView?.layoutManager =
                        LinearLayoutManager(this@MyLeavesActivity)
                    myLeaveHistoryRecyclerView?.adapter = myLeaveHistoryAdapter
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
                    arrayHistory?.let { myTeamLeaveHistoryAdapter.submitData(it) }
                    myTeamLeaveHistoryRecyclerView?.setHasFixedSize(true)
                    myTeamLeaveHistoryRecyclerView?.layoutManager =
                        LinearLayoutManager(this@MyLeavesActivity)
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

    private fun initUIListener() {

        myLeavesTextView.setOnClickListener {

            myLeavesTextView.setTextColor(Color.parseColor("#000000"))
            myLeavesUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            myTeamLeavesTextView.setTextColor(Color.parseColor("#708792"))
            myTeamLeavesUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            myLeaveHistoryConstraintLayout.visibility = View.VISIBLE
            myTeamLeaveHistoryConstraintLayout.visibility = View.GONE
            leaveRequestButton.visibility = View.VISIBLE
        }


        myTeamLeavesTextView.setOnClickListener {

            myTeamLeavesTextView.setTextColor(Color.parseColor("#000000"))
            myTeamLeavesUnderlineView.setBackgroundColor(Color.parseColor("#000000"))

            myLeavesTextView.setTextColor(Color.parseColor("#708792"))
            myLeavesUnderlineView.setBackgroundColor(Color.parseColor("#708792"))

            myLeaveHistoryConstraintLayout.visibility = View.GONE
            myTeamLeaveHistoryConstraintLayout.visibility = View.VISIBLE
            leaveRequestButton.visibility = View.GONE
        }

        myTeamLeaveHistoryAdapter.approvedOnItemClick = { model ->
            model?.leave_type?.id?.let { approveLeave(it) }
        }

        myTeamLeaveHistoryAdapter.rejectedOnItemClick = { model ->
            model?.leave_type?.id?.let { rejectLeave(it) }
        }

        myLeaveHistoryAdapter.editLeaveRequestOnItemClick = { model ->
            // model?.leave_type?.id?.let { approveLeave(it) }
        }

        myLeaveHistoryAdapter.deleteLeaveRequestOnItemClick = { model ->
            model?.let { showDeleteLeaveRequestDialog(it) }
        }
    }

    private fun approveLeave(leaveId: Int) {
        try {
            val call = apiInterface?.approveLeave(leaveId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@MyLeavesActivity,
                            "Successfully Approved",
                            Toast.LENGTH_LONG
                        ).show()

                        // refresh data
                        initCurrentDate(DateUtils().addOneMonth(currentIndex).time)

                    } else {
                        Toast.makeText(
                            this@MyLeavesActivity,
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        this@MyLeavesActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                this@MyLeavesActivity,
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun rejectLeave(leaveId: Int) {
        try {
            val call = apiInterface?.rejectLeave(leaveId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@MyLeavesActivity,
                            "Successfully Rejected",
                            Toast.LENGTH_LONG
                        ).show()
                        // refresh data
                        initCurrentDate(DateUtils().addOneMonth(currentIndex).time)
                    } else {
                        Toast.makeText(
                            this@MyLeavesActivity,
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        this@MyLeavesActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                this@MyLeavesActivity,
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteLeaveRequest(leaveId: Int) {
        try {
            val call = apiInterface?.deleteLeave(leaveId)
            call?.enqueue(object : retrofit2.Callback<Any?> {
                override fun onResponse(
                    call: retrofit2.Call<Any?>,
                    response: retrofit2.Response<Any?>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@MyLeavesActivity,
                            "Successfully Deleted",
                            Toast.LENGTH_LONG
                        ).show()
                        // refresh data
                        initCurrentDate(DateUtils().addOneMonth(currentIndex).time)

                    } else {
                        Toast.makeText(
                            this@MyLeavesActivity,
                            "Something happens wrong please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<Any?>,
                    t: Throwable
                ) {
                    call.cancel()
                    Toast.makeText(
                        this@MyLeavesActivity,
                        "Something happens wrong please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            ex.message
            Toast.makeText(
                this@MyLeavesActivity,
                "Something happens wrong please try again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showDeleteLeaveRequestDialog(model: MyLeaveHistoryModel) {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.delete_leave_request_dilaog)
        //val body = dialog.findViewById(R.id.body) as TextView
        //body.text = title
        val yesBtn = dialog.findViewById(R.id.yesButtonDeleteLeaveRequestDialog) as Button
        val cencelBtn = dialog.findViewById(R.id.cancelButtonDeleteLeaveRequestDialog) as Button
        yesBtn.setOnClickListener {
            model.id?.let { it1 -> deleteLeaveRequest(it1) }
            dialog.dismiss()
        }
        cencelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }
}