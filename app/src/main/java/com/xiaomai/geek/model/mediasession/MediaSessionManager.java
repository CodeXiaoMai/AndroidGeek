package com.xiaomai.geek.model.mediasession;

import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.xiaomai.geek.model.musicPlayer.IMusicItem;
import com.xiaomai.geek.model.musicPlayer.MusicPlayService;

public class MediaSessionManager {
    private static final String TAG = "MediaSessionManager";
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private MusicPlayService playService;
    private MediaSessionCompat mediaSession;

    public static MediaSessionManager get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static MediaSessionManager instance = new MediaSessionManager();
    }

    private MediaSessionManager() {
    }

    public void init(MusicPlayService playService) {
        this.playService = playService;
        setupMediaSession();
    }

    private void setupMediaSession() {
        mediaSession = new MediaSessionCompat(playService, TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mediaSession.setCallback(callback);
        mediaSession.setActive(true);
    }

    public void updatePlaybackState() {
        int state = playService.isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        mediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, playService.getPosition(), 1)
                        .build());
    }

    public void updateMetaData(IMusicItem music) {
        if (music == null) {
            mediaSession.setMetadata(null);
            return;
        }

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getName())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, music.getName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, music.getName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, playService.getDuration())/*
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, music.getName())*/;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AppCache.get().getLocalMusicList().size());
        }

        mediaSession.setMetadata(metaData.build());
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            playService.start();
        }

        @Override
        public void onPause() {
            playService.pause();
        }

        @Override
        public void onSkipToNext() {
            playService.next(true);
        }

        @Override
        public void onSkipToPrevious() {
            playService.previous();
        }

        @Override
        public void onStop() {
            playService.stop();
        }

        @Override
        public void onSeekTo(long pos) {
            playService.seekTo((int) pos);
        }
    };
}