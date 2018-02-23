package com.xiaomai.geek.common

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.DrawableRes
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.xiaomai.geek.R
import com.xiaomai.geek.databinding.GeekTitleViewBinding
import kotlinx.android.synthetic.main.geet_title_menu.view.*

/**
 * Created by wangce on 2018/1/30.
 */
class GeekTitleView : FrameLayout {

    private var binding: GeekTitleViewBinding

    private var popupWindow: PopupWindow? = null

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

    fun setBackView(@DrawableRes backRes: Int = R.drawable.common_title_ic_back, listener: OnClickListener?) {
        binding.btBack.setImageResource(backRes)
        binding.btBack.setOnClickListener(listener)
    }

    /**
     * 如果同时设置 menu 和 iconRes，展示 menu
     */
    fun setMenu(menu: String? = null,
                @DrawableRes iconRes: Int? = R.drawable.common_title_ic_more_vert,
                listener: OnClickListener?) {
        if (!menu.isNullOrEmpty()) {
            binding.tvMore.apply {
                text = menu
                visibility = View.VISIBLE
                setOnClickListener {
                    listener?.onClick(it)
                    popupWindow?.dismiss()
                }
            }
        } else if (iconRes != null) {
            binding.btMore.apply {
                setImageResource(iconRes)
                visibility = View.VISIBLE
                setOnClickListener {
                    listener?.onClick(it)
                    popupWindow?.dismiss()
                }
            }
        }
    }

    fun addMenu(menu: String, @DrawableRes iconRes: Int? = null, listener: OnClickListener?) {
        val menuItem = MenuItem(menu, iconRes, listener)
        menuList.add(menuItem)
        if (menuList.size == 1) {
            binding.btMore.visibility = View.VISIBLE
            binding.btMore.setOnClickListener {
                popupWindow = PopupWindow(this).apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    contentView = createContentView()
                    setBackgroundDrawable(ColorDrawable(0x00000000))
                    isOutsideTouchable = true
                    isFocusable = true
                }
                popupWindow?.showAtLocation(this, Gravity.TOP + Gravity.END, 10, 45)
            }
        }
    }

    private fun createContentView(): View? {
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        menuList.forEach { menuItem ->
            val itemView = LayoutInflater.from(context).inflate(R.layout.geet_title_menu, container, false)
            if (menuItem.iconRes != null) {
                itemView.icon.setImageResource(menuItem.iconRes)
                itemView.icon.visibility = View.VISIBLE
            } else {
                itemView.icon.visibility = View.GONE
            }
            itemView.title.text = menuItem.menu
            itemView.setOnClickListener {
                menuItem.listener?.onClick(itemView)
                popupWindow?.dismiss()
            }

            container.addView(itemView)
        }


        val root = CardView(context).apply {
            radius = 5f
            addView(container)
        }
        return root
    }

    fun removeAllMenus() {
        menuList.clear()
    }

    private var menuList = arrayListOf<MenuItem>()

    private data class MenuItem(val menu: String,
                                @DrawableRes val iconRes: Int? = null,
                                val listener: OnClickListener?)
}