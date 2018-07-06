package com.xiaomai.geek.model.musicPlayer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

/**
 * 监听来电和拨打电话的状态，需要在 manifests 中注册监听，需要添加 android.permission.READ_PHONE_STATE 权限
 */
class PhoneStateBroadcastReceiver : BroadcastReceiver() {

    var callback: Callback? = null

    override fun onReceive(context: Context, intent: Intent) {
        val telephonyManager =
            context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        // 主叫电话
        if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            callback?.onCallPhone(phoneNumber)
        } else {
            // 被叫电话
            var inComingNumber = ""
            when (telephonyManager.callState) {
            // 响铃
                TelephonyManager.CALL_STATE_RINGING -> {
                    inComingNumber = intent.getStringExtra("incoming_number") ?: ""
                    callback?.onCallPhone(inComingNumber)
                }
            // 正在接通
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    callback?.onOffHook(inComingNumber)
                }
            // 挂断
                TelephonyManager.CALL_STATE_IDLE -> {
                    callback?.onHangPhone()
                }
            }
        }
    }

    interface Callback {
        fun onCallPhone(phoneNumber: String)
        // 接通中
        fun onOffHook(phoneNumber: String)

        fun onHangPhone()
    }
}
