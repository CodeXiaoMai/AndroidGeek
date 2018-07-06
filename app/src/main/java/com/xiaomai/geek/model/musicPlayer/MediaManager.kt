package com.xiaomai.geek.model.musicPlayer

import android.app.PendingIntent
import android.content.*
import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.RemoteControlClient
import android.media.RemoteController
import android.media.session.PlaybackState
import android.os.Build
import android.os.IBinder
import android.support.annotation.IntDef
import android.view.KeyEvent
import java.text.FieldPosition

class MediaManager(var context: Context?, var callback: Callback?) {

    private var componentName: ComponentName? = null
    private var audioManager: AudioManager? = null
    private var remoteControlClient: RemoteControlClient? = null
    private val onAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> callback?.onAudioFocusGain()
            AudioManager.AUDIOFOCUS_LOSS -> callback?.onAudioFocusLoss()
        // 短暂失去焦点，例如短信提示
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> callback?.onAudioFocusLossTransient()
        }
    }

    private val mediaButtonReceiver = MediaButtonReceiver()

    fun registerReceiver() {
        if (context == null) return
        componentName = ComponentName(context!!.packageName, MediaButtonReceiver::class.java.name)
        audioManager = context!!.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager?.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        audioManager?.registerMediaButtonEventReceiver(componentName)
        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.component = componentName
        val pendingIntent = PendingIntent.getBroadcast(context!!, 0, mediaButtonIntent, 0)
        remoteControlClient = RemoteControlClient(pendingIntent)
        audioManager?.registerRemoteControlClient(remoteControlClient)

        remoteControlClient?.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                        or RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                        or RemoteControlClient.FLAG_KEY_MEDIA_NEXT
                        or RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
        )

        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.MEDIA_BUTTON")
        context?.registerReceiver(mediaButtonReceiver, intentFilter)
    }

    fun setPlaybackState(state: Int, position: Long, playbackSpeed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            remoteControlClient?.setPlaybackState(state, position, playbackSpeed)
        }
    }

    fun setMetadata(bitmap: Bitmap?, artist: String?, title: String?, duration: Long) {
        val metadata = remoteControlClient?.editMetadata(true) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && bitmap != null) {
            metadata.putBitmap(RemoteController.MetadataEditor.BITMAP_KEY_ARTWORK, bitmap)
        }
        metadata.apply {
            putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, artist)
            putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title)
            putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, duration)
        }.apply()
    }

    fun unRegisterReceiver() {
        audioManager?.unregisterRemoteControlClient(remoteControlClient)
        audioManager?.unregisterMediaButtonEventReceiver(componentName)
        audioManager?.abandonAudioFocus(onAudioFocusChangeListener)
        context?.unregisterReceiver(mediaButtonReceiver)
        context = null
    }

    class MediaButtonReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_MEDIA_BUTTON == action) {
                val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                if (keyEvent.action == KeyEvent.ACTION_UP) {
                    when (keyEvent.keyCode) {
                        KeyEvent.KEYCODE_HEADSETHOOK,
                        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                            (peekService(context, Intent(context, MusicPlayService::class.java))
                                    as MusicPlayService.MusicPlayBinder).getService().changePlayStatus()
                        }
                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            (peekService(context, Intent(context, MusicPlayService::class.java))
                                    as MusicPlayService.MusicPlayBinder).getService().pause()
                        }
                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            (peekService(context, Intent(context, MusicPlayService::class.java))
                                    as MusicPlayService.MusicPlayBinder).getService().start()
                        }
                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            (peekService(context, Intent(context, MusicPlayService::class.java))
                                    as MusicPlayService.MusicPlayBinder).getService().next()
                        }
                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            (peekService(context, Intent(context, MusicPlayService::class.java))
                                    as MusicPlayService.MusicPlayBinder).getService().previous()
                        }
                    }
                }
            }
        }
    }

    interface Callback {
        fun onAudioFocusLossTransient()
        fun onAudioFocusGain()
        fun onAudioFocusLoss()
        fun onGetPlaybackPosition(): Long?
    }
}