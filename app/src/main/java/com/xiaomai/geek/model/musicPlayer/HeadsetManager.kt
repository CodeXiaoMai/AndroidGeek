package com.xiaomai.geek.model.musicPlayer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

class HeadsetManager(var context: Context?, var callback: Callback?) {

    private var headsetReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED == action) {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                // 需要 android.permission.BLUETOOTH 权限
                if (BluetoothProfile.STATE_CONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                    callback?.onHeadsetIn()
                } else if (BluetoothProfile.STATE_DISCONNECTED == bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                    callback?.onHeadsetOut()
                }
            } else if (Intent.ACTION_HEADSET_PLUG == action) {
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) != 0) {
                        callback?.onHeadsetIn()
                    }
                }
            } else if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == action) {
                callback?.onHeadsetOut()
            }
        }
    }

    fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        context?.registerReceiver(headsetReceiver, intentFilter)
    }

    fun unRegisterReceiver() {
        context?.unregisterReceiver(headsetReceiver)
        headsetReceiver = null
        context = null
        callback = null
    }

    interface Callback {
        fun onHeadsetIn()
        fun onHeadsetOut()
    }
}