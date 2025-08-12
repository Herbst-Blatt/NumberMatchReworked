package com.example.numbermatchreworked

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toColorInt
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


class BoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private var numbersPerRow = 10
    private var rowCount = 10
    private var startNumberCount = 47
    private var fontSize = 75

    private var curMaxHeight = 0F
    private var cellSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1
    private var selectedNr = -1
    private var prevSelectedRow = -1
    private var prevSelectedCol = -1
    private var prevSelectedNr = -1

    private var gameNumberArray = arrayListOf<Int>()
    private var gameSolvedArray = arrayListOf<Boolean>()



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

    private val wrongSelectionPaint = Paint().apply {
        style = Paint.Style.FILL
        color = "#BA3D25".toColorInt()
    }
    private val correctSelectionPaint = Paint().apply {
        style = Paint.Style.FILL
        color = "#84DB4D".toColorInt()
    }


    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        textSize = fontSize.toFloat()
    }

    private val solvedTextPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
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
            for (i in 1 until startNumberCount){
                gameNumberArray += Random.nextInt(1,9)
                gameSolvedArray += false
            }

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
        var paint: Paint
        if (!gameSolvedArray[r*numbersPerRow+c]){
            paint = textPaint
        } else {
            paint = solvedTextPaint
        }
        canvas.drawText(
            gameNumberArray[c + r * numbersPerRow].toString(),
            c * cellSizePixels + (cellSizePixels - fontSize),
            (r + 1) * cellSizePixels - (cellSizePixels - fontSize),
            paint
        )
    }


    private fun highlightSelectedCell(r: Int, c: Int, canvas: Canvas) {
        if (selectedCol == -1 || selectedRow == -1 ) return
        if (r == selectedRow && c == selectedCol) // only selected cell
            if (selectedCol != prevSelectedCol || selectedRow != prevSelectedRow) { // only if selected cell isn't already selected
                when (checkCorrectSelection()) {
                    0 -> { // CASE: correct!
                        gameSolvedArray[prevSelectedRow*numbersPerRow + prevSelectedCol] = true
                        gameSolvedArray[selectedRow*numbersPerRow + selectedCol] = true

                        fillCell(canvas, selectedRow,selectedCol, correctSelectionPaint)
                        fillCell(canvas, prevSelectedRow,prevSelectedCol, correctSelectionPaint)
                    }
                    1 -> { // CASE: wrong combo
                        fillCell(canvas, selectedRow,selectedCol, wrongSelectionPaint)
                        fillCell(canvas, prevSelectedRow,prevSelectedCol, wrongSelectionPaint)
                    }
                    2 -> { // CASE: selected first cell
                        fillCell(canvas, r, c, selectedCellPaint)
                    }
                }

            } else { // CASE: unselect current cell
                selectedCol = -1
                selectedRow = -1
                prevSelectedCol = -1
                prevSelectedRow = -1
                prevSelectedNr = -1
                selectedNr = -1
            }
    }

    private fun fillCell(canvas: Canvas, row: Int, col: Int, paint: Paint) {
        canvas.drawRect( // fill cell
            col * cellSizePixels,
            row * cellSizePixels,
            (col + 1) * cellSizePixels,
            (row + 1) * cellSizePixels,
            paint
        )
        canvas.drawRect( // redraw outline as is covered by fill
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
        prevSelectedCol = selectedCol
        prevSelectedRow = selectedRow
        selectedCol = (x/cellSizePixels).toInt()
        selectedRow = (y/cellSizePixels).toInt()

        prevSelectedNr = selectedNr
        selectedNr = gameNumberArray[selectedRow * numbersPerRow + selectedCol]

        invalidate()
    }



    /**
    return:
     - 0 -> correct Match!
     - 1 -> wrong match
     - 2 -> only one selection, nothing to check yet
     */
    private fun checkCorrectSelection():Int {
        if (prevSelectedNr == -1 ) return 2//first selection

        if (prevSelectedNr == selectedNr || prevSelectedNr + selectedNr == 10) {
            if (checkDiagonalMatch()) return 0
            if (checkHorizontalMatch()) return 0
            if (checkVerticalMatch()) return 0
        }
        return 1 // wrong match
    }

    private fun checkDiagonalMatch(): Boolean {
        return false
    }

    private fun checkHorizontalMatch(): Boolean {
        return false
    }

    private fun checkVerticalMatch(): Boolean {
        if (prevSelectedCol != selectedCol) return false
        if (checkBetweenVer()) return true
        return false
    }

    private fun checkBetweenVer(): Boolean {
        val lower = min(prevSelectedRow, selectedRow) +1
        val higher = max(prevSelectedRow, selectedRow) -1
        var cur: Int
        for (i in lower until higher) {
            cur = i * numbersPerRow + selectedCol
            if (!gameSolvedArray[cur]) return false
        }
        return true
    }
}