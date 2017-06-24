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

    private var ivBack: ImageView? = null
    private var ivPlay: ImageView? = null
    private var videoView: IjkVideoView? = null
    private var imageView: ImageView? = null
    private var tvName: TextView? = null

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
                videoView?.setVideoURI(Uri.parse(video.url))
                tvName?.text = video.name
            }
        }
    }

    private fun initView() {
        ivBack = findViewById(R.id.iv_back) as ImageView
        ivPlay = findViewById(R.id.iv_play) as ImageView
        videoView = findViewById(R.id.video_view) as IjkVideoView
        imageView = findViewById(R.id.imageView) as ImageView
        tvName = findViewById(R.id.tv_name) as TextView

        ivBack?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }
        })

        ivPlay?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                videoView?.start()
                ivPlay?.visibility = View.GONE
            }
        })
    }
}