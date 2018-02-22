package com.xiaomai.geek.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.xiaomai.geek.R
import com.xiaomai.geek.common.utils.WidgetUtils
import com.xiaomai.geek.databinding.GeekMenuItemViewBinding

/**
 * Created by wangce on 2018/2/22.
 */
class MenuItemView : FrameLayout {

    private var binding: GeekMenuItemViewBinding

    private var title: String? = null

    private var iconDrawable: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MenuItemView)
        val drawable = typedArray.getDrawable(R.styleable.MenuItemView_iconDrawable)
        title = typedArray.getString(R.styleable.MenuItemView_menuTitle)
        typedArray.recycle()

        val inflater = LayoutInflater.from(context)
        binding = GeekMenuItemViewBinding.inflate(inflater, this@MenuItemView, true)

        drawable?.apply {
            setIconDrawable(this)
        }

        if (!title.isNullOrEmpty()) {
            binding.geekMenuTitle.text = title
        }

        binding.geekMenuTitle.setTextColor(WidgetUtils.createColorStateList(
                context,
                android.R.color.black,
                R.color.blue,
                android.R.color.darker_gray
        ))
    }

    fun setIconDrawable(iconDrawable: Drawable) {
        this.iconDrawable = iconDrawable

        binding.geekMenuIcon.setImageDrawable(WidgetUtils.createStateListDrawable(
                context, iconDrawable, android.R.color.darker_gray, R.color.blue
        ))
    }

    override fun setSelected(selected: Boolean) {
        if (isSelected == selected) {
            return
        }
        super.setSelected(selected)

        binding.geekMenuTitle.isSelected = selected
        binding.geekMenuIcon.isSelected = selected
    }
}