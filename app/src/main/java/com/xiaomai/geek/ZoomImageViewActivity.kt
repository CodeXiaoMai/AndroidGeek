package com.xiaomai.geek

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.zoom_imageview_demo.*

class ZoomImageViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zoom_imageview_demo)

        zoomImageView.sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.demo)
    }
}