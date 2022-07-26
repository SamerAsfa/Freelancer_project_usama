package com.example.myapplication.myapplication.data

import android.content.Context
import android.content.res.Configuration
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.models.ActionModel
import com.example.myapplication.myapplication.ui.fragments.HomeFragment
import java.util.*

object AppUtils {


    fun getTextRes(state: String): Int {
        if (state == PublicKeys.PIN) {
            return R.string.punch_in
        } else if (state == PublicKeys.POUT) {
            return R.string.Punch_Out
        } else if (state == PublicKeys.BREAK_IT) {
            return R.string.break_in
        } else if (state == PublicKeys.BREAK_OUT) {
            return R.string.break_out
        } else if (state == PublicKeys.LEAVE_IN) {
            return R.string.leave_in
        } else {
            return R.string.leave_out
        }
    }

    fun changeLang(context: Context, lang: String?) {
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val config = Configuration()
        config.setLocale(myLocale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    fun getTextBody(
        punchOut: String,
        timeIs: String,
        time: String,
        doYouWantTo: String,
        now: String,
    ): String {
        return String.format(
            Locale.CANADA,
            "%s %s %s %s %s %s",
            punchOut,
            timeIs,
            time,
            doYouWantTo,
            punchOut,
            now
        )
    }


    fun actionToTake(actionModel: ActionModel?): HomeFragment.ButtonState {
        val keyState = actionModel?.key
        if (keyState == PublicKeys.PIN) {
            return HomeFragment.ButtonState.PUNCH_IN
        } else if (keyState == PublicKeys.POUT) {
            return HomeFragment.ButtonState.PUNCH_OUT
        } else if (keyState == PublicKeys.BREAK_IT) {
            return HomeFragment.ButtonState.BREAK_IN
        } else if (keyState == PublicKeys.BREAK_OUT) {
            return HomeFragment.ButtonState.BREAK_OUT
        } else if (keyState == PublicKeys.LEAVE_IN) {
            return HomeFragment.ButtonState.LEAVE_IN
        } else {
            return HomeFragment.ButtonState.LEAVE_OUT
        }
    }


}