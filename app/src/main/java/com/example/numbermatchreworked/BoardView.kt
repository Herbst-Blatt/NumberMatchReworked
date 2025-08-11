package com.example.numbermatchreworked

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toColorInt
import kotlin.random.Random


class BoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private var numbersPerRow = 10
    private var rowCount = 10
    private var startNumberCount = 47
    private var fontSize = 75
    private var curMaxHeight = 0F
    private var cellSizePixels = 0F

    private var selectedRow = 0
    private var selectedCol = 0

    private var gameNumberArray = listOf<Int>()



    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 5F
    }
    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 3F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL
        color = "#C9A0DC".toColorInt()
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = fontSize.toFloat()
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
        fillGameNumberArray()
        fillCells(canvas)
    }

    private fun fillGameNumberArray() {
        if (gameNumberArray.isEmpty())
            for (i in 1 until startNumberCount)
                gameNumberArray += Random.nextInt(1,9)

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

    private fun fillCells(canvas: Canvas) {
        for (r in 0 until 4)
            for(c in 0 until numbersPerRow) {

                highlightSelectedCell(r, c, canvas)
                writeNumbers(canvas, c, r)
            }
    }

    private fun writeNumbers(canvas: Canvas, c: Int, r: Int) {
        canvas.drawText(
            gameNumberArray[c + r * numbersPerRow].toString(),
            c * cellSizePixels + (cellSizePixels - fontSize),
            (r + 1) * cellSizePixels - (cellSizePixels - fontSize),
            textPaint
        )
    }

    private fun highlightSelectedCell(r: Int, c: Int, canvas: Canvas) {
        if (selectedCol == -1 || selectedRow == -1 ) return
        if (r == selectedRow && c == selectedCol)
            fillCell(canvas, r, c, selectedCellPaint)
    }

    private fun fillCell(canvas: Canvas, row: Int, col: Int, paint: Paint) {
        canvas.drawRect(
            col * cellSizePixels,
            row * cellSizePixels,
            (col + 1) * cellSizePixels,
            (row + 1) * cellSizePixels,
            paint
        )
        canvas.drawRect(
            col * cellSizePixels,
            row * cellSizePixels,
            (col + 1) * cellSizePixels,
            (row + 1) * cellSizePixels,
            thinLinePaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        selectedCol = (x/cellSizePixels).toInt()
        selectedRow = (y/cellSizePixels).toInt()
        invalidate()
    }
    
}