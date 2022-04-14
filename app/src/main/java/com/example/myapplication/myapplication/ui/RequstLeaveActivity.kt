package com.example.myapplication.myapplication.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.models.TypesModel
import com.example.myapplication.myapplication.ui.adapters.LeavesAdapter
import com.example.myapplication.myapplication.ui.adapters.TypeAdapter
import kotlinx.android.synthetic.main.activity_my_leaves.*
import kotlinx.android.synthetic.main.activity_requst_leave.*
import java.util.*

class RequstLeaveActivity : AppCompatActivity() {
    private var fromDatePicker: DatePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requst_leave)

        leaveDateTextView.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val dpd = DatePickerDialog(
                    this@RequstLeaveActivity,
                    { datePicker, i, i1, i2 ->
                        fromDatePicker = datePicker
                        leaveDateTextView.text = String.format(
                            Locale.CANADA,
                            "%s,%s%s",
                            DateUtils().dayDate(datePicker.dayOfMonth+1),
                            DateUtils().monthDate(datePicker.month+1),
                            DateUtils().yearDate(datePicker.year),
                        )
                    }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                    calendar[Calendar.DATE]
                )
                dpd.show()
            } catch (ex: Exception) {
                ex.message
            }
        }

        fromSelectTextView.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val dpd = TimePickerDialog(
                    this@RequstLeaveActivity,
                    { datePicker, HOUR, MINUTE  ->
                        fromSelectTextView.text = String.format(
                            Locale.CANADA,
                            "%s:%s",
                            HOUR,
                            MINUTE,
                        )
                    }, calendar[Calendar.HOUR], calendar[Calendar.MINUTE],
                    false
                )
                dpd.show()
            } catch (ex: Exception) {
                ex.message
            }

        }

        toSelectTextView.setOnClickListener {
            try {
                val calendar = Calendar.getInstance()
                val dpd = TimePickerDialog(
                    this@RequstLeaveActivity,
                    { datePicker, HOUR, MINUTE  ->
                        toSelectTextView.text = String.format(
                            Locale.CANADA,
                            "%s:%s",
                            HOUR,
                            MINUTE,
                        )
                    }, calendar[Calendar.HOUR], calendar[Calendar.MINUTE],
                    false
                )
                dpd.show()
            } catch (ex: Exception) {
                ex.message
            }

        }


        leaveTypeSelectTextView.setOnClickListener {
            POSTMediasTask().get(
                this,
                BaseRequest.leaveTypeApi,
                "",
                object : ResponseApi {
                    override fun onSuccessCall(response: String?) {
                        val arrayHistory: ArrayList<TypesModel?>? = response?.let {
                            TypesModel().parseArray(
                                it
                            )
                        }
                        shopperList(arrayHistory)
                    }

                    override fun onErrorCall(error: VolleyError?) {
                        print("")
                    }
                }
            )
        }
    }


      fun shopperList(shopperArrayList: ArrayList<TypesModel?>?) {
        if (shopperArrayList != null && shopperArrayList.size > 0) {
            val dialogAndroidAppCus = Dialog(this)
            dialogAndroidAppCus.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogAndroidAppCus.setContentView(R.layout.dialog_types)
            val recyclerView =
                dialogAndroidAppCus.findViewById<View>(R.id.recyclerView) as RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val realtyStringAdapter = TypeAdapter( shopperArrayList, object : TypeAdapter.Clicks {
                override fun click(typesModel: TypesModel, position: Int) {

                }
            })
            recyclerView.adapter = realtyStringAdapter
            dialogAndroidAppCus.setCancelable(true)
            dialogAndroidAppCus.show()
        }
    }


}