package com.xiaomai.geek.model.musicPlayer

data class MusicItem(
        private var name: String,
        private var url: String
) : IMusicItem {
    override fun getName(): String = name

    override fun getUrl(): String = url
}