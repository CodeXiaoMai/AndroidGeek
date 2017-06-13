package com.xiaomai.geek.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.mukesh.MarkdownView
import com.xiaomai.geek.R
import com.xiaomai.geek.data.pref.ThemePref
import com.xiaomai.geek.ui.module.articel.WebViewActivity

/**
 * Created by XiaoMai on 2017/6/13.
 */
class SupportMarkdownView : MarkdownView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val typedValue: TypedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.clockBackgroundPrimary, typedValue, true)
        setBackgroundColor(typedValue.resourceId)
        setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (ThemePref.getTheme(context) == R.style.AppTheme)
                    return
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    evaluateJavascript(WebViewActivity.JAVASCRIPT, null)
                } else {
                    loadUrl("javascript:" + WebViewActivity.JAVASCRIPT)
                }
            }
        })
    }

    private var lastX: Int = 0
    private var lastY: Int = 0

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            val rawX = ev.rawX
            val rawY = ev.rawY

            val action = ev.action and MotionEvent.ACTION_MASK
            when (action) {
                MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = rawX - lastX
                    val deltaY = rawY - lastY
                    if (Math.abs(deltaX) < Math.abs(deltaY)) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
            lastX = rawX.toInt()
            lastY = rawY.toInt()
        }
        return super.dispatchTouchEvent(ev)
    }
}