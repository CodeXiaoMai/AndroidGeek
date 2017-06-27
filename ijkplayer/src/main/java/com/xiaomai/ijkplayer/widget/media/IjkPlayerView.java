package com.xiaomai.ijkplayer.widget.media;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomai.ijkplayer.R;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by XiaoMai on 2017/6/27.
 */

public class IjkPlayerView extends FrameLayout implements View.OnClickListener {

    // 关联的Activity
    private Activity mAttachActivity;
    private IjkVideoView mVideoView;
    private TextView mTvTitle;
    private ImageView mIvPlay;

    public IjkPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public IjkPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initMediaPlayer();
    }

    public IjkPlayerView setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public IjkPlayerView setVideoPath(String url) {
        return setVideoPath(Uri.parse(url));
    }

    public IjkPlayerView setVideoPath(Uri uri) {
        mVideoView.setVideoURI(uri);
        return this;
    }

    public void start() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            mIvPlay.setSelected(true);
        }
        // 视频播放时开启屏幕常亮
        mAttachActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void pause() {
        mIvPlay.setSelected(false);
        if (mVideoView.isPlaying())
            mVideoView.pause();
        // 视频暂停时关闭屏幕常亮
        mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void stop() {
        pause();
        mVideoView.stopPlayback();
    }

    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    public void seekTo(int position) {
        mVideoView.seekTo(position);
    }


    private void initMediaPlayer() {
        // 加载 IjkMediaPlayer 库
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

    }

    private void initView(Context context) {
        mAttachActivity = (Activity) context;
        View view = View.inflate(context, R.layout.ijk_player_view, this);
        mVideoView = (IjkVideoView) view.findViewById(R.id.ijk_video_view);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mIvPlay = (ImageView) view.findViewById(R.id.iv_play);

        mIvPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvPlay) {
            togglePlayStatus();
        }
    }

    private void togglePlayStatus() {
        if (isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    // 进度条最大值
    private static final int MAX_VIDEO_SEEK = 1000;
    // 默认隐藏控制栏时间
    private static final int DEFAULT_HIDE_TIMEOUT = 3000;
    // 更新进度消息
    private static final int MSG_UPDATE_SEEK = 10086;
    // 使能翻转消息
    private static final int MSG_ENABLE_ORIENTATION = 10087;
    // 尝试重连消息
    private static final int MSG_TRY_RELOAD = 10088;
    // 无效变量
    private static final int INVALID_VALUE = -1;
    // 达到文件时长的允许误差值，用来判断是否播放完成
    private static final int INTERVAL_TIME = 1000;
    // 设置水印
    private static final int MSG_SET_MARK_IMAGE = 10089;

}
