package io.charts

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

abstract class ChartView : View {

    protected var dataPointHorizontalSpacing = 0f
    protected var dataPointHeightFactor = 0f
    protected open var chartInset = 0f

    protected val paint = Paint().apply {
        color = Color.BLACK
    }

    open var data: List<Float> = listOf()
        set(value) {
            field = value
            calculateDataPointData()
            invalidate()
        }

    @ColorInt
    var color: Int = 0
        set(value) {
            field = value
            paint.color = color
            invalidate()
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        calculateDataPointData()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec), resolveSize(desiredHeight, heightMeasureSpec))
    }

    protected fun calculateDataPointData() {
        dataPointHorizontalSpacing = (width - paddingLeft - paddingRight) / data.size.toFloat()
        dataPointHeightFactor = (height - chartInset - paddingBottom - paddingTop) / (data.max() ?: 1f)
    }
}
