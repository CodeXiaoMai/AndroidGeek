package com.xiaomai.geek.ui.module.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xiaomai.geek.R
import com.xiaomai.geek.data.module.Video
import com.xiaomai.geek.ui.base.BaseActivity
import com.xiaomai.ijkplayer.widget.media.IjkPlayerView
import com.xiaomai.ijkplayer.widget.media.IjkVideoView

/**
 * Created by XiaoMai on 2017/6/24.
 */
class VideoDetailActivity : BaseActivity() {

    companion object {
        val EXTRA_VIDEO: String = "EXTRA_VIDEO"

        fun launch(video: Video?, context: Context) {
            val intent : Intent = Intent(context, VideoDetailActivity::class.java)
            intent.putExtra(VideoDetailActivity.EXTRA_VIDEO, video)
            context.startActivity(intent)
        }
    }

    private var videoView: IjkPlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        initView()
        loadData()
    }

    private fun loadData() {
        intent?.let {
            val video = intent.getParcelableExtra<Video>(EXTRA_VIDEO)
            video?.let {
                videoView?.setVideoPath(video.url)
                        ?.setTitle(video.name)
                        ?.start()
            }
        }
    }

    private fun initView() {
        videoView = findViewById(R.id.ijk_player_view) as IjkPlayerView
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.onDestroy()
    }
}