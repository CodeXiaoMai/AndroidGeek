package com.xiaomai.ijkplayer.widget.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiaomai.ijkplayer.R;
import com.xiaomai.ijkplayer.callback.OnShareClickListener;
import com.xiaomai.ijkplayer.utils.TimeUtils;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by XiaoMai on 2017/6/27.
 */

public class IjkPlayerView extends FrameLayout implements View.OnClickListener {

    // 关联的Activity
    private Activity mAttachActivity;
    private IjkVideoView mVideoView;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvShare;
    private ImageView mIvPlay;
    private TextView mTvCurrentTime;
    private TextView mTvTotalTime;
    private SeekBar mSeekBar;
    private ImageView mIvFullScreen;

    private String mTitle;
    private String mUrl;
    private boolean mIsForceFullScreen;
    private boolean mIsSeeking;

    // 手势控制
    private GestureDetector mGestureDetector;

    private OnShareClickListener mOnShareClickListener;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_SEEK:
                    updateSeekProgress();
                    msg = obtainMessage(MSG_UPDATE_SEEK);
                    sendMessageDelayed(msg, 1000);
                    break;
            }
        }
    };

    private void updateSeekProgress() {
        // 视频播放的当前进度
        int currentPosition = mVideoView.getCurrentPosition();
        // 视频的总时长
        int duration = mVideoView.getDuration();
        if (duration > 0) {
            long pos = (long) MAX_VIDEO_SEEK * currentPosition / duration;
            mSeekBar.setProgress((int) pos);
        }
        // 获取缓冲的进度百分比
        int percentage = mVideoView.getBufferPercentage();
        mSeekBar.setSecondaryProgress(percentage * 10);
        mTvCurrentTime.setText(TimeUtils.generateTime(currentPosition));
        mTvTotalTime.setText(TimeUtils.generateTime(duration));
    }

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
        mTitle = title;
        return this;
    }

    public IjkPlayerView setVideoPath(String url) {
        mUrl = url;
        return setVideoPath(Uri.parse(url));
    }

    public IjkPlayerView setVideoPath(Uri uri) {
        mVideoView.setVideoURI(uri);
        return this;
    }

    public IjkPlayerView setForceFullScreen(boolean isForceFullScreen) {
        mIsForceFullScreen = isForceFullScreen;
        if (mIsForceFullScreen) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mIvFullScreen.setVisibility(GONE);
        } else {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mIvFullScreen.setVisibility(VISIBLE);
        }
        return this;
    }

    public void start() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            mIvPlay.setSelected(true);

            mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
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

        mGestureDetector = new GestureDetector(mAttachActivity, mGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        private boolean isDownTouch;

        // 是否是横向滑动
        private boolean isHorizontal;

        private boolean isVolume;

        @Override
        public boolean onDown(MotionEvent e) {
            isDownTouch = true;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isDownTouch) {
                isHorizontal = Math.abs(distanceX) > Math.abs(distanceY);
                // 如果是屏幕左半部分就是音量
                isVolume = e1.getX() > getResources().getDisplayMetrics().widthPixels * 0.5f;
                isDownTouch = false;
            }

            if (isHorizontal) {
                int currentPosition = mVideoView.getCurrentPosition();
                int duration = mVideoView.getDuration();
                // 单次滑动最大时间差为100秒或播放时长的1/2
                long deltaMax = Math.min(100 * 1000, duration / 2);
                // 滑动的时间
                long delta = (long) (-deltaMax * distanceX / mVideoView.getWidth());
                long targetPosition = delta + currentPosition;
                if (targetPosition > duration) {
                    targetPosition = duration;
                } else if (targetPosition < 0) {
                    targetPosition = 0;
                }
                seekTo((int) targetPosition);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    private void initView(Context context) {
        mAttachActivity = (Activity) context;
        View view = View.inflate(context, R.layout.ijk_player_view, this);
        mIvBack = (ImageView) view.findViewById(R.id.iv_back);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvTitle.setSelected(true);
        mIvShare = (ImageView) view.findViewById(R.id.iv_share);
        mVideoView = (IjkVideoView) view.findViewById(R.id.ijk_video_view);
        mIvPlay = (ImageView) view.findViewById(R.id.iv_play);
        mTvCurrentTime = (TextView) view.findViewById(R.id.tv_current_time);
        mTvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mIvFullScreen = (ImageView) view.findViewById(R.id.iv_full_screen);

        mIvBack.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvFullScreen.setOnClickListener(this);

        mSeekBar.setMax(MAX_VIDEO_SEEK);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private long newPosition;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                long duration = mVideoView.getDuration();
                newPosition = duration * progress / MAX_VIDEO_SEEK;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsSeeking = true;
                mHandler.removeMessages(MSG_UPDATE_SEEK);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsSeeking = false;
                seekTo((int) newPosition);
                updateSeekProgress();
                mHandler.sendEmptyMessage(MSG_UPDATE_SEEK);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mIvBack) {
            // 如果强制横屏就直接退出，否则判断当前是否为横屏，如果横屏退出横屏，否则finish
            if (mIsForceFullScreen) {
                mAttachActivity.finish();
            } else {
                if (mAttachActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    mAttachActivity.finish();
                }
            }
        } else if (v == mIvShare) {
            if (mOnShareClickListener != null) {
                mOnShareClickListener.onShareClick(mTitle, mUrl);
            }
        } else if (v == mIvPlay) {
            togglePlayStatus();
        } else if (v == mIvFullScreen) {
            mAttachActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public void setOnShareClickListener(OnShareClickListener listener) {
        mOnShareClickListener = listener;
    }

    private void togglePlayStatus() {
        if (isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    public void onDestroy() {
        mVideoView.destroy();
        IjkMediaPlayer.native_profileEnd();
        mHandler.removeMessages(MSG_UPDATE_SEEK);
        mHandler.removeMessages(MSG_TRY_RELOAD);
        mAttachActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
