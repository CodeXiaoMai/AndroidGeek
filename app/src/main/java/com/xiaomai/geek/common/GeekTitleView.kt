package com.xiaomai.geek.common

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.xiaomai.geek.R
import com.xiaomai.geek.databinding.GeekTitleViewBinding

/**
 * Created by wangce on 2018/1/30.
 */
class GeekTitleView : FrameLayout {

    private lateinit var binding: GeekTitleViewBinding

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        val inflater = LayoutInflater.from(context)
        binding = GeekTitleViewBinding.inflate(inflater, this, true)
        binding.tvTitle.isSelected = true

        binding.btBack.setOnClickListener {
            if (context is Activity) {
                context.finish()
            }
        }

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TitleView)
        val title = typedArray.getString(R.styleable.TitleView_title)
        val menuDrawable = typedArray.getDrawable(R.styleable.TitleView_menuIcon)
        typedArray.recycle()

        menuDrawable?.let {
            binding.btBack.setImageDrawable(it)
        }

        if (!TextUtils.isEmpty(title)) {
            setTitle(title)
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setOnBackClickListener(listener: OnClickListener?) {
        binding.btBack.visibility = if (listener == null) View.INVISIBLE else View.VISIBLE
        binding.btBack.setOnClickListener(listener)
    }

    fun setOnMoreClickListener(listener: OnClickListener?) {
        binding.btMore.visibility = if (listener == null) View.INVISIBLE else View.VISIBLE
        binding.btMore.setOnClickListener(listener)
    }
}