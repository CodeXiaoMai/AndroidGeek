package com.xiaomai.geek.ui.widget.kotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by wangce on 2018/1/25.
 */
class FlowLayout : ViewGroup {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        val cCount = childCount

        var layoutParams: MarginLayoutParams
        var cWidth: Int
        var cHeight: Int

        var lineWidth = 0
        var lineHeight = 0

        var resultWidth = 0
        var resultHeight = 0

        for (i in 0 until cCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                if (i == cCount - 1) {
                    resultHeight += lineHeight
                    resultWidth = Math.max(resultWidth, lineWidth)
                }
                continue
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            layoutParams = child.layoutParams as MarginLayoutParams
            cWidth = child.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
            cHeight = child.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin

            if (lineWidth + cWidth > sizeWidth - paddingLeft - paddingRight) {
                resultHeight += lineHeight
                resultWidth = Math.max(resultWidth, lineWidth)
                lineWidth = cWidth
                lineHeight = cHeight
            } else {
                lineWidth += cWidth
                lineHeight = Math.max(lineHeight, cHeight)
            }

            if (i == cCount - 1) {
                resultHeight += lineHeight
                resultWidth = Math.max(resultWidth, lineWidth)
            }
        } // end for

        resultWidth += paddingLeft + paddingRight
        resultHeight += paddingTop + paddingBottom

        setMeasuredDimension(
                if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else resultWidth,
                if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else resultHeight
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val cCount = childCount

        var cWidth = 0
        var cHeight = 0
        var marginLayoutParams: MarginLayoutParams

        var lineHeight = 0
        var lineTop = paddingTop
        var lineWidth = 0

        var cLeft = 0
        var cTop = 0

        for (i in 0 until cCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            marginLayoutParams = child.layoutParams as MarginLayoutParams
            cWidth = child.measuredWidth + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin
            cHeight = child.measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin

            if (lineWidth + cWidth > width - paddingLeft - paddingRight) {
                lineTop += lineHeight
                lineHeight = cHeight
                cTop = lineTop + marginLayoutParams.topMargin
                cLeft = paddingLeft + marginLayoutParams.leftMargin
                lineWidth = cWidth
                child.layout(cLeft, cTop, cLeft + child.measuredWidth, cTop + child.measuredHeight)
            } else {
                cTop = lineTop + marginLayoutParams.topMargin
                cLeft = lineWidth + marginLayoutParams.leftMargin
                lineWidth += cWidth
                lineHeight = Math.max(lineHeight, cHeight)
                child.layout(cLeft, cTop, cLeft + child.measuredWidth, cTop + child.measuredHeight)
            }
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}