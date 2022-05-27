package com.example.myapplication.myapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.error.VolleyError
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.base.BaseFragment
import com.example.myapplication.myapplication.base.ResponseApi
import com.example.myapplication.myapplication.data.BaseRequest
import com.example.myapplication.myapplication.data.DateUtils
import com.example.myapplication.myapplication.data.POSTMediasTask
import com.example.myapplication.myapplication.models.HistoryModel
import com.example.myapplication.myapplication.ui.adapters.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_history.view.*
import java.util.*

class HistoryFragment : BaseFragment() {

    companion object {
        val fragmentName : String = "HistoryFragment"

        val historyFragment: HistoryFragment? = null
        fun getInstance(): HistoryFragment {
            return historyFragment ?: HistoryFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print("")
    }

    protected var mainView: View? = null
    var currentIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        mainView = view
        initCurrentDate(Date().time)
        view.vectorRightImageView.setOnClickListener {
            increaseMonth()
        }
        view.vectorLeftImageView.setOnClickListener {
            decreaseMonth()
        }

        return view
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
        mainView?.dateTextView?.text = DateUtils().getDateFromTimeStamp(timeStamp)
        val monthYear = DateUtils().getDateForApiFromTimeStamp(timeStamp)
        getMonth(monthYear)
    }

    private fun getMonth(monthYear: String) {
        POSTMediasTask().get(
            requireActivity(),
            BaseRequest.punchHistoryApi,
            "/${monthYear}",
            object : ResponseApi {
                override fun onSuccessCall(response: String?) {
                    val arrayHistory: ArrayList<HistoryModel?>? = response?.let {
                        HistoryModel().parseArray(
                            it
                        )
                    }
                    val scanMainAdapter = HistoryAdapter(arrayHistory)
                    mainView?.historyRecyclerView?.setHasFixedSize(true)
                    mainView?.historyRecyclerView?.setLayoutManager(LinearLayoutManager(context))
                    mainView?.historyRecyclerView?.adapter = scanMainAdapter
                }

                override fun onErrorCall(error: VolleyError?) {
                    showDialogOneButtonsCustom("Error", error?.message.toString(), "Ok") { dialog ->
                        dialog.dismiss()
                    }
                }
            }
        )
    }


}