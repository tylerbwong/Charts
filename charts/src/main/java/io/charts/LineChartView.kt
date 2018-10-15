package io.charts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent

class LineChartView : ChartView {

    private val scrubberPaint = Paint().apply {
        isAntiAlias = true
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = DEFAULT_STROKE_WIDTH
    }
    private val path = Path()
    private var xPos = 0f
    private var yPos = 0f
    private var isScrubbing = false

    override var data: List<Float> = listOf()
        set(value) {
            field = value
            calculateDataPointData()
            calculatePath()
            invalidate()
        }

    var strokeWidth: Float = DEFAULT_STROKE_WIDTH
        set(value) {
            field = value
            paint.strokeWidth = field
            chartInset = field
            invalidate()
        }

    override var chartInset: Float = strokeWidth
        set(value) {
            field = value
            halfChartInset = field / 2
        }
    private var halfChartInset = chartInset / 2

    var pathCornerRadius: Float = DEFAULT_CORNER_RADIUS
        set(value) {
            field = value
            paint.pathEffect = CornerPathEffect(field)
            invalidate()
        }

    init {
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = this@LineChartView.strokeWidth
            strokeCap = Paint.Cap.ROUND
            pathEffect = CornerPathEffect(pathCornerRadius)
        }
        isFocusable = true
        isFocusableInTouchMode = true
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        with(context.obtainStyledAttributes(attrs, R.styleable.LineChartView)) {
            color = getColor(R.styleable.LineChartView_lineColor, 0)
            strokeWidth = getFloat(R.styleable.LineChartView_strokeWidth, DEFAULT_STROKE_WIDTH)
            pathCornerRadius = getFloat(R.styleable.LineChartView_pathCornerRadius, DEFAULT_CORNER_RADIUS)
            recycle()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        calculatePath()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)

        if (isScrubbing) {
            canvas.drawLine(xPos, halfChartInset, xPos, height.toFloat() - halfChartInset, scrubberPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> event.startScrubbing()
            MotionEvent.ACTION_MOVE -> event.startScrubbing()
            MotionEvent.ACTION_UP -> stopScrubbing()
        }
        return true
    }

    private fun MotionEvent.startScrubbing() {
        val index = actionIndex
        xPos = getX(index)
        yPos = getY(index)
        isScrubbing = true
        invalidate()
    }

    private fun stopScrubbing() {
        xPos = 0f
        yPos = 0f
        isScrubbing = false
        invalidate()
    }

    private fun calculatePath() {
        var xPos = paddingLeft.toFloat()

        path.reset()
        data.forEachIndexed { index, dataPoint ->
            val midPointOffset = dataPointHorizontalSpacing / 2
            val yPos = height - halfChartInset - dataPoint * dataPointHeightFactor - paddingBottom

            if (index == 0) {
                path.moveTo(xPos + midPointOffset, yPos)
            } else {
                path.lineTo(xPos + midPointOffset, yPos)
            }

            xPos += dataPointHorizontalSpacing
        }
    }

    companion object {
        private const val DEFAULT_STROKE_WIDTH = 8f
        private const val DEFAULT_CORNER_RADIUS = 16f
    }
}
