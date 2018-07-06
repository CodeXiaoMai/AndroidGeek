package com.xiaomai.geek.model.musicPlayer

import android.content.Context
import android.content.IntentFilter

class PhoneStateManager(var context: Context, var callback: PhoneStateBroadcastReceiver.Callback?) {

    private var phoneStateBroadcastReceiver: PhoneStateBroadcastReceiver? = null

    fun registerReceiver() {
        phoneStateBroadcastReceiver = PhoneStateBroadcastReceiver()
        phoneStateBroadcastReceiver?.callback = callback
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL")
        intentFilter.addAction("android.intent.action.PHONE_STATE")
        context.registerReceiver(phoneStateBroadcastReceiver, intentFilter)
    }

    fun unRegisterReceiver() {
        context.unregisterReceiver(phoneStateBroadcastReceiver)
    }

}