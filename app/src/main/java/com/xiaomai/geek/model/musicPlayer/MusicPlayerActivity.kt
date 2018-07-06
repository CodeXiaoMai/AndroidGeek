package com.xiaomai.geek.model.musicPlayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.xiaomai.geek.R
import com.xiaomai.geek.common.invisible
import com.xiaomai.geek.common.visible
import com.xiaomai.geek.common.visibleElseInvisible
import kotlinx.android.synthetic.main.article_item.*
import kotlinx.android.synthetic.main.music_player_activity.*

class MusicPlayerActivity : AppCompatActivity() {

    private var musicPlayService: MusicPlayService? = null
    private var isUserTouchSeekBar = false

    private val callback = object : OnPlayCallback {
        override fun onPrepared(mediaPlayer: MediaPlayer, iMusicItem: IMusicItem) {
            tv_current_time.text = "00:00"
            tv_duration.text = TimeUtils.millToTime(musicPlayService!!.getDuration()).removePrefix("00:")
            tv_audio_name.text = iMusicItem.getName()
            tv_intro.text = iMusicItem.getName()
            seek_bar.progress = 0
        }

        override fun onStart() {
            bt_play_pause.setImageResource(R.drawable.btn_player_playing)
        }

        override fun onPause() {
            bt_play_pause.setImageResource(R.drawable.btn_player_suspend)
        }

        override fun onStop() {
            onPause()
        }

        override fun onCompletion() {
            onPause()
        }

        override fun onError(mediaPlayer: MediaPlayer?, message: String) {
            onPause()
        }

        override fun onDurationChanged(position: Int, duration: Int, progress: Int) {
            var newPosition = position
            if (newPosition > duration) {
                newPosition = duration
            }
            tv_duration.text = TimeUtils.millToTime(duration).removePrefix("00:")
            tv_current_time.text = TimeUtils.millToTime(newPosition).removePrefix("00:")
            if (!isUserTouchSeekBar) {
                seek_bar.progress = progress
            }
        }

        override fun onStopTimeChanged(time: Long) {
            tv_hint_stop_time.visibleElseInvisible(time > 0)
            tv_hint_stop_time.text = "将在" + TimeUtils.millToTime((time + 1000).toInt()).removePrefix("00:") + "后自动停止"
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            musicPlayService?.onPlayCallback = null
            musicPlayService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            musicPlayService = (service as MusicPlayService.MusicPlayBinder).getService()

            val playList = mutableListOf<IMusicItem>()
            playList.add(MusicItem("1.mp3", Environment.getExternalStorageDirectory().absolutePath + "/1.mp3"))
            playList.add(MusicItem("2.mp3", Environment.getExternalStorageDirectory().absolutePath + "/1.mp3"))
            playList.add(MusicItem("3.mp3", Environment.getExternalStorageDirectory().absolutePath + "/1.mp3"))
            playList.add(MusicItem("4.mp3", Environment.getExternalStorageDirectory().absolutePath + "/1.mp3"))
            playList.add(MusicItem("5.mp3", Environment.getExternalStorageDirectory().absolutePath + "/1.mp3"))

            musicPlayService?.setPlayList(playList)
            musicPlayService?.onPlayCallback = callback
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_player_activity)

        val intent = Intent(this, MusicPlayService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        bt_play_pause.setOnClickListener {
            musicPlayService?.changePlayStatus()
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            private var newProgress = 0
            private var newPosition = 0

            private lateinit var currentTime: String
            private lateinit var durationTime: String

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    newProgress = progress
                    val duration = musicPlayService!!.getDuration()
                    newPosition = duration * newProgress / 100
                    currentTime = TimeUtils.millToTime(newPosition).removePrefix("00:")
                    durationTime = TimeUtils.millToTime(duration).removePrefix("00:")
                    tv_hint.text = currentTime + "/" + durationTime
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isUserTouchSeekBar = true
                tv_hint.visible()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isUserTouchSeekBar = false
                musicPlayService!!.seekTo(newPosition)
                tv_hint.postDelayed(({
                    runOnUiThread {
                        tv_hint.invisible()
                    }
                }), 1000)
                tv_current_time.text = currentTime
                tv_duration.text = durationTime
            }
        })

        bt_play_pause.setOnClickListener {
           musicPlayService?.changePlayStatus()
        }

        bt_last.setOnClickListener {
            musicPlayService?.previous()
        }

        bt_next.setOnClickListener {
           musicPlayService?.next(true)
        }

        changePlayMode(true)

        bt_loop_model.setOnClickListener {
            musicPlayService?.changePlayMode()
            changePlayMode()
        }

        bt_timer.setOnClickListener {

        }
    }

    /**
     * @param init true 不显示toast
     */
    private fun changePlayMode(init: Boolean = false) {
        val playMode = musicPlayService?.playMode
        var playModeString = ""
        when (playMode) {
            PlayMode.NORMAL -> {
                playModeString = "顺序播放"
                bt_loop_model.setImageResource(R.drawable.btn_player_order_play)
            }
            PlayMode.LOOP -> {
                playModeString = "列表循环"
                bt_loop_model.setImageResource(R.drawable.btn_player_loop)
            }
            PlayMode.SINGLE -> {
                playModeString = "单曲循环"
                bt_loop_model.setImageResource(R.drawable.btn_player_single)
            }
        }
        if (!init) {
            tv_play_mode?.removeCallbacks(runnable)
            tv_play_mode?.text = playModeString
            tv_play_mode?.visible()
            tv_play_mode?.postDelayed(runnable, 2000)
        }
    }

    private val runnable = Runnable {
        tv_play_mode?.invisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}