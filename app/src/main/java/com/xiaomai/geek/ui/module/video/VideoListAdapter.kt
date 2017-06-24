package com.xiaomai.geek.ui.module.video

import android.content.Intent
import android.view.View
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
            holder.setOnClickListener(R.id.parent, object : View.OnClickListener{
                override fun onClick(v: View?) {
                    val context = holder.convertView.context
                    VideoDetailActivity.launch(video, context)
                }
            })
        }
    }
}