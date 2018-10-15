package io.charts

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class HistogramView : ChartView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        with(context.obtainStyledAttributes(attrs, R.styleable.HistogramView)) {
            color = getColor(R.styleable.HistogramView_barColor, 0)
            recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        var xPos = paddingLeft.toFloat()

        data.forEach {
            val barHeight = it * dataPointHeightFactor

            canvas.drawRect(
                xPos,
                height - barHeight - paddingTop,
                xPos + dataPointHorizontalSpacing,
                height.toFloat() - paddingBottom, paint
            )

            xPos += dataPointHorizontalSpacing
        }
    }
}
