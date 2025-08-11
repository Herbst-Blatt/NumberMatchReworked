package com.example.numbermatchreworked

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class BoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private var numbersPerRow = 10
    private var rowCount = 10
    private var curMaxHeight = 0F
    private var cellSizePixels = 0F

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }
    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 3F
    }


    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / numbersPerRow).toFloat()
        curMaxHeight = cellSizePixels * rowCount
        canvas.drawRect(
            0F,
            0F,
            width.toFloat(),
            curMaxHeight,
            thickLinePaint
        )

        drawCells(canvas)
    }

    private fun drawCells(canvas: Canvas) {
        for (i in 1 until numbersPerRow) {
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                curMaxHeight,
                thinLinePaint
            )
        }
        for (i in 1 until rowCount) {
            canvas.drawLine(
                0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                thinLinePaint
            )
        }
    }

    // ensure square board - which I don't want
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        setMeasuredDimension(width, (cellSizePixels*rowCount).toInt())
//    }
}