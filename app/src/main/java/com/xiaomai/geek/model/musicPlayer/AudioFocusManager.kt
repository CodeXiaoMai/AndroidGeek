package com.xiaomai.geek.model.musicPlayer

import android.content.Context

class AudioFocusManager(var context: Context?, var callback: Callback) {



    interface Callback {
        fun onAudioFocusLossTransient()
        fun onAudioFocusGain()
        fun onAudioFocusLoss()
    }
}