package com.xiaomai.geek.model.musicPlayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.session.PlaybackState
import android.net.Uri
import android.os.*
import java.io.File

class MusicPlayService : Service() {

    private val binder = MusicPlayBinder()
    private var headsetManager: HeadsetManager? = null
    private var mediaManager: MediaManager? = null
    private var phoneStateManager: PhoneStateManager? = null
    var onPlayCallback: OnPlayCallback? = null

    private var mediaPlayer: MediaPlayer? = null
    private var playList: MutableList<IMusicItem>? = null
    private var currentItem: IMusicItem? = null
    var playMode: PlayMode = PlayMode.NORMAL
        private set
    private var handler: Handler = Handler()
    var stopTime: Long = System.currentTimeMillis()

    private val runnable = object : Runnable {
        override fun run() {
            val localMusicPlayer = mediaPlayer
            localMusicPlayer?.also {
                onPlayCallback?.onDurationChanged(
                    it.currentPosition,
                    it.duration,
                    (it.currentPosition * 1.0f / it.duration * 100).toInt()
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (it.isPlaying) {
                        mediaManager?.setPlaybackState(
                            PlaybackState.STATE_PLAYING,
                            it.currentPosition.toLong(),
                            1.0f
                        )
                    } else {
                        mediaManager?.setPlaybackState(
                            PlaybackState.STATE_PAUSED,
                            it.currentPosition.toLong(),
                            1.0f
                        )
                    }
                }
            }

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerHeadSetListener()
        registerMediaReceiver()
        registerPhoneStateReceiver()
        handler.post(runnable)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = binder

    inner class MusicPlayBinder : Binder() {
        fun getService() = this@MusicPlayService
    }

    fun setPlayList(playList: MutableList<IMusicItem>?, playMode: PlayMode = PlayMode.NORMAL) {
        if (this.playList != playList) {
            this.playList = playList
            if (playList != null && playList.isNotEmpty()) {
                currentItem = playList[0]
                initMediaPlayer()
            }
        }
    }

    private fun initMediaPlayer() {
        release()
        val localCurrentItem = currentItem ?: return
        if (!localCurrentItem.getUrl().startsWith("http://") && !localCurrentItem.getUrl().startsWith(
                "https://"
            )
        ) {
            if (!File(localCurrentItem.getUrl()).exists()) {
                onPlayCallback?.onError(null, "文件不存在")
                return
            }
        }
        val localMediaPlayer =
            MediaPlayer.create(this@MusicPlayService, Uri.fromFile(File(localCurrentItem.getUrl())))
                    ?: return
        localMediaPlayer.setWakeMode(this@MusicPlayService, PowerManager.PARTIAL_WAKE_LOCK)
        localMediaPlayer.setOnPreparedListener { player ->
            onPlayCallback?.onPrepared(player, localCurrentItem)
            mediaManager?.setMetadata(
                null,
                currentItem?.getName(),
                currentItem?.getName(),
                getDuration().toLong()
            )
            start()
        }
        localMediaPlayer.setOnErrorListener { mp, what, extra ->
            onPlayCallback?.onError(mp, "")
            return@setOnErrorListener true
        }
        localMediaPlayer.setOnCompletionListener {
            onPlayCallback?.onCompletion()
            next()
        }
        mediaPlayer = localMediaPlayer
    }

    fun next(isFromUser: Boolean = false) {
        currentItem = getNextMusic(isFromUser)
        if (currentItem == null) {
            currentItem = getNextMusic(true)
        }
        initMediaPlayer()
    }

    private fun getNextMusic(isFromUser: Boolean = false): IMusicItem? {
        val localPlayList = playList ?: return null
        var nextPosition = localPlayList.indexOf(currentItem) + 1

        when (playMode) {
            PlayMode.NORMAL -> {
                if (nextPosition < 0 || nextPosition >= localPlayList.size) {
                    if (isFromUser) {
                        nextPosition = 0
                    } else {
                        return null
                    }
                }
            }
            PlayMode.LOOP -> {
                if (nextPosition < 0 || nextPosition >= localPlayList.size) {
                    nextPosition = 0
                }
            }
            PlayMode.SINGLE -> {
                nextPosition = localPlayList.indexOf(currentItem)
            }
        }
        return localPlayList[nextPosition]
    }

    fun start() {
        mediaPlayer?.start()
        onPlayCallback?.onStart()
    }

    fun pause() {
        mediaPlayer?.pause()
        onPlayCallback?.onPause()
    }

    fun stop() {
        mediaPlayer?.stop()
        onPlayCallback?.onStop()
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun previous() {
        currentItem = getPreviousMusic()
        initMediaPlayer()
    }

    fun changePlayMode(): PlayMode {
        playMode = when (playMode) {
            PlayMode.NORMAL -> PlayMode.LOOP
            PlayMode.LOOP -> PlayMode.SINGLE
            PlayMode.SINGLE -> PlayMode.NORMAL
        }
        return playMode
    }

    fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying ?: false
        } catch (e: IllegalStateException) {
            false
        }
    }

    fun changePlayStatus() {
        val localMediaPlayer = mediaPlayer ?: return
        if (localMediaPlayer.isPlaying) {
            pause()
        } else {
            start()
        }
    }

    fun getDuration() = mediaPlayer?.duration ?: 0

    fun getPosition() = mediaPlayer?.currentPosition ?: 0

    private fun release() {
        stop()
        mediaPlayer = null
    }

    private fun getPreviousMusic(): IMusicItem? {
        val localPlayList = playList ?: return null
        var lastPosition = localPlayList.indexOf(currentItem) - 1

        if (lastPosition < 0) {
            lastPosition = localPlayList.size - 1
        }

        return localPlayList[lastPosition]
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
        headsetManager?.unRegisterReceiver()
        mediaManager?.unRegisterReceiver()
        phoneStateManager?.unRegisterReceiver()
    }

    private fun registerPhoneStateReceiver() {
        phoneStateManager = PhoneStateManager(this, object : PhoneStateBroadcastReceiver.Callback {

            // 如果是被动暂停，再次获取焦点时开始播放
            var waitStart = false

            override fun onCallPhone(phoneNumber: String) {
                if (isPlaying()) {
                    waitStart = true
                    pause()
                } else {
                    waitStart = false
                }
            }

            override fun onOffHook(phoneNumber: String) {
            }

            override fun onHangPhone() {
                if (waitStart && !isPlaying()) {
                    start()
                }
            }
        })
        phoneStateManager?.registerReceiver()
    }

    private fun registerMediaReceiver() {
        mediaManager = MediaManager(this, object : MediaManager.Callback {

            // 如果是被动暂停，再次获取焦点时开始播放
            var waitStart = false

            override fun onAudioFocusLossTransient() {
            }

            override fun onAudioFocusGain() {
                if (waitStart && !isPlaying()) {
                    start()
                }
            }

            override fun onAudioFocusLoss() {
                if (isPlaying()) {
                    waitStart = true
                    pause()
                } else {
                    waitStart = false
                }
            }

            override fun onGetPlaybackPosition(): Long? {
                return mediaPlayer?.currentPosition?.toLong()
            }
        })
        mediaManager?.registerReceiver()
    }

    private fun registerHeadSetListener() {
        headsetManager = HeadsetManager(this, object : HeadsetManager.Callback {
            override fun onHeadsetIn() {
            }

            override fun onHeadsetOut() {
                pause()
            }
        })
        headsetManager?.registerReceiver()
    }
}