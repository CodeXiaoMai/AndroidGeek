package com.xiaomai.geek.model.musicPlayer

import android.media.MediaPlayer

interface OnPlayCallback {
    fun onPrepared(mediaPlayer: MediaPlayer, iMusicItem: IMusicItem)
    fun onStart()
    fun onPause()
    fun onStop()
    fun onCompletion()
    fun onError(mediaPlayer: MediaPlayer?, message: String)
    fun onDurationChanged(position: Int, duration: Int, progress: Int)
    fun onStopTimeChanged(time: Long)
}