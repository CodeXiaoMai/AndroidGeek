package com.xiaomai.geek.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.absoluteValue

class ZoomImageView : View {

    companion object {
        // 初始化状态常量
        const val STATUS_INIT = 1
        // 图片放大状态常量
        const val STATUS_ZOOM_OUT = 2
        // 图片缩小状态常量
        const val STATUS_ZOOM_IN = 3
        // 图片拖动状态常量
        const val STATUS_MOVE = 4
    }

    // 用于对图片进行移动或缩放变换的矩阵
    private val tempMatrix = Matrix()

    // 待展示的Bitmap对象
    var sourceBitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    // 记录当前操作的状态
    private var currentStatus = STATUS_INIT

    // ZoomImageView 控件的高度
    // ZoomImageView 控件的宽度

    // 记录两个手指同时放到屏幕上时，中心点的横坐标值
    private var centerPointX = 0f

    // 记录两个手指同时放到屏幕上时，中心点的纵坐标
    private var centerPointY = 0f
    // 记录当前图片的宽度，图片被缩放时，这个值会一起变
    private var currentBitmapWidth = 0f
    // 记录当前图片的高度，图片被缩放时，这个值会一起变
    private var currentBitmapHeight = 0f

    // 记录上次手指移动时的横坐标
    private var lastXMove = -1f

    // 记录上次手指移动时的纵坐标
    private var lastYMove = -1f

    // 记录手指在横坐标方向上的移动距离
    private var movedDistanceX = 0f

    // 记录手指在纵坐标方向上的移动距离
    private var movedDistanceY = 0f

    // 记录图片在矩阵上的横向偏移值
    private var totalTranslateX = 0f

    // 记录图片在矩阵上的纵向偏移值
    private var totalTranslateY = 0f

    // 记录图片在矩阵上的总缩放比例
    private var totalRatio = 0f

    // 记录手指移动的距离造成的缩放比例
    private var scaledRatio = 0f

    // 记录图片初始化时的缩放比例
    private var initRatio = 0f

    // 记录上次两个手指之间的距离
    private var lastFingerDistance = 0.0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (currentStatus) {
            STATUS_ZOOM_IN,
            STATUS_ZOOM_OUT -> zoom(canvas)
            STATUS_MOVE -> move(canvas)
            STATUS_INIT -> initBitmap(canvas)
            else -> canvas?.drawBitmap(sourceBitmap, tempMatrix, null)
        }
    }

    private fun zoom(canvas: Canvas?) {
        val bitmap = sourceBitmap ?: return
        tempMatrix.reset()
        // 将图片按总方法比例进行缩放
        tempMatrix.postScale(totalRatio, totalRatio)
        val scaledWidth = bitmap.width * totalRatio
        val scaledHeight = bitmap.height * totalRatio
        var translateX = 0f
        var translateY = 0f
        // 如果当前图片宽度小于屏幕宽度，则按屏幕中心的横坐标进行水平缩放，否则按两个手指的中心点的横坐标进行水平缩放
        if (currentBitmapWidth < width) {
            translateX = (width - scaledWidth).div(2f)
        } else {
            translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio)
            // 进行边界检查，保证图片缩放后在水平方向上不会偏移出屏幕
            if (translateX > 0) {
                translateX = 0f
            } else if (width - translateX > scaledWidth) {
                translateX = width - scaledWidth
            }
        }
        // 如果当前图片高度小于屏幕高度，则按照屏幕中心的纵坐标进行垂直缩放，否则按照两个手指的中心点进行垂直缩放
        if (currentBitmapHeight < height) {
            translateY = (height - scaledHeight).div(2f)
        } else {
            translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio)
            // 进行边界检查，保证图片缩放后在垂直方向上不会偏移出屏幕
            if (translateY > 0) {
                translateY = 0f
            } else if (height - translateY > scaledHeight) {
                translateY = height - scaledHeight
            }
        }

        // 缩放后对图片进行偏移，以保证缩放后中心点位置不变
        tempMatrix.postTranslate(translateX, translateY)
        totalTranslateX = translateX
        totalTranslateY = translateY
        currentBitmapWidth = scaledWidth
        currentBitmapHeight = scaledHeight
        canvas?.drawBitmap(bitmap, tempMatrix, null)
    }

    /**
     * 对图片进行平移处理
     */
    private fun move(canvas: Canvas?) {
        tempMatrix.reset()
        // 根据手指移动的距离计算出总偏移值
        val translateX = totalTranslateX + movedDistanceX
        val translateY = totalTranslateY + movedDistanceY
        // 先按照已有的缩放比例对图拍进行缩放
        tempMatrix.postScale(totalRatio, totalRatio)
        // 再根据移动距离进行偏移
        tempMatrix.postTranslate(translateX, translateY)
        totalTranslateX = translateX
        totalTranslateY = translateY
        canvas?.drawBitmap(sourceBitmap, tempMatrix, null)
    }

    private fun initBitmap(canvas: Canvas?) {
        val bitmap = sourceBitmap ?: return

        tempMatrix.reset()
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        if (bitmapWidth > width || bitmapHeight > height) {
            if (bitmapWidth - width > bitmapHeight - height) {
                // 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它完全显示
                val ratio = width.div(bitmapWidth.toFloat())
                tempMatrix.postScale(ratio, ratio)
                val translateY = (height - bitmapHeight * ratio).div(2f)
                // 在纵坐标上进行偏移，以保证图片居中显示
                tempMatrix.postTranslate(0f, translateY)
                totalTranslateY = translateY
                totalRatio = ratio
                initRatio = ratio
            } else {
                // 当图片高度大于屏幕高度时，将图片等比例缩放，使它可以完全显示
                val ratio = height.div(bitmapHeight.toFloat())
                tempMatrix.postScale(ratio, ratio)
                val translateX = (width - bitmapWidth * ratio).div(2f)
                tempMatrix.postTranslate(translateX, 0f)
                totalTranslateX = translateX
                totalRatio = ratio
                initRatio = ratio
            }
            currentBitmapWidth = bitmapWidth * initRatio
            currentBitmapHeight = bitmapHeight * initRatio
        } else {
            // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
            val translateX = (width - bitmapWidth).div(2f)
            val translateY = (height - bitmapHeight).div(2f)
            tempMatrix.postTranslate(translateX, translateY)
            totalTranslateX = translateX
            totalTranslateY = translateY

            totalRatio = 1f
            initRatio = 1f
            currentBitmapWidth = bitmapWidth.toFloat()
            currentBitmapHeight = bitmapHeight.toFloat()
        }
        canvas?.drawBitmap(bitmap, tempMatrix, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val actionMasked = event.actionMasked
        when (actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    // 当有两个手指按在屏幕上时，计算两指之间的距离
                    lastFingerDistance = distanceBetweenFingers(event)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1) {
                    // 只有一个手指在屏幕上移动，为拖动状态
                    val xMove = event.x
                    val yMove = event.y
                    if (lastXMove == -1f && lastYMove == -1f) {
                        lastXMove = xMove
                        lastYMove = yMove
                    }
                    currentStatus = STATUS_MOVE
                    movedDistanceX = xMove - lastXMove
                    movedDistanceY = yMove - lastYMove
                    // 进行边界检查，不允许将图片拖出边界
                    if (totalTranslateX + movedDistanceX > 0) {
                        movedDistanceX = 0f
                    } else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth) {
                        movedDistanceX = 0f
                    }

                    if (totalTranslateY + movedDistanceY > 0) {
                        movedDistanceY = 0f
                    } else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
                        movedDistanceY = 0f
                    }
                    // 调用 onDraw() 方法绘制图片
                    invalidate()
                    lastXMove = xMove
                    lastYMove = yMove
                } else if (event.pointerCount == 2) {
                    // 有两个手指按在屏幕上移动时，为缩放状态
                    centerPointBetweenFingers(event)
                    val fingerDis = distanceBetweenFingers(event)
                    currentStatus = if (fingerDis > lastFingerDistance) {
                        STATUS_ZOOM_OUT
                    } else {
                        STATUS_ZOOM_IN
                    }
                    // 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例
                    if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
                            || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
                        scaledRatio = fingerDis.div(lastFingerDistance).toFloat()
                        totalRatio *= scaledRatio
                        if (totalRatio > 4 * initRatio) {
                            totalRatio = 4 * initRatio
                        } else if (totalRatio < initRatio) {
                            totalRatio = initRatio
                        }
                        // 调用 onDraw() 方法绘制图片
                        invalidate()
                        lastFingerDistance = fingerDis
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount == 2) {
                    // 手指离开屏幕时将临时值还原
                    lastXMove = -1f
                    lastYMove = -1f
                }
            }
            MotionEvent.ACTION_UP -> {
                lastXMove = -1f
                lastYMove = -1f
            }
        }
        return true
    }

    /**
     * 计算两个手指之间的距离
     */
    private fun distanceBetweenFingers(event: MotionEvent): Double {
        val disX = (event.getX(0) - event.getX(1)).absoluteValue
        val disY = (event.getY(0) - event.getY(1)).absoluteValue

        return Math.sqrt((disX * disX + disY * disY).toDouble())
    }

    private fun centerPointBetweenFingers(event: MotionEvent) {
        val xPoint0 = event.getX(0)
        val yPoint0 = event.getY(0)
        val xPoint1 = event.getX(1)
        val yPoint1 = event.getY(1)

        centerPointX = (xPoint0 + xPoint1).div(2f)
        centerPointY = (yPoint0 + yPoint1).div(2f)
    }
}