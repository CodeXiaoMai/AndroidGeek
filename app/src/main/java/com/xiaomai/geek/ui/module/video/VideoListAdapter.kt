package com.xiaomai.geek.ui.module.video

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiaomai.geek.R
import com.xiaomai.geek.data.module.Video

/**
 * Created by XiaoMai on 2017/6/23.
 */
class VideoListAdapter : BaseQuickAdapter<Video> {

    constructor(data: MutableList<Video>?) : super(R.layout.item_video, data)

    override fun convert(holder: BaseViewHolder?, video: Video?) {
        holder?.let {
//            holder.setImageResource
            holder.setText(R.id.name, video?.name)
        }
    }
}