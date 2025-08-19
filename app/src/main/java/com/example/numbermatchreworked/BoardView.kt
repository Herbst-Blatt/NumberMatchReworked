package com.example.numbermatchreworked

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toColorInt
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


class BoardView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private var numbersPerRow = 10
    private var rowCount = 10
    private var numberCount = -1
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
    private var gameBlockArray = arrayListOf<Boolean>()

    private var matchFound = false
    /**
     * This variable is used to identify how the correct solution was done and allows to draw the correct line
     * - true -> basic line, horizontal, vertical or diagonal
     * - false -> line overflow
     */
    private var typeBasicMatch : Boolean= false



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

    private val solutionLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 15F
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
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
        color = Color.LTGRAY
        textSize = fontSize.toFloat()
    }

    private val wrongTextPaint = Paint().apply {
        style = Paint.Style.FILL
        color = "#BA3D25".toColorInt()
        textSize = fontSize.toFloat()
    }

    private fun onCreate(){
        fillGameNumberArray()
    }

    /**
     * Draws the canvas new after each user interaction
     */
    override fun onDraw(canvas: Canvas) {
        if (gameNumberArray.isEmpty()){
            onCreate()
        }

        cellSizePixels = (width / numbersPerRow).toFloat()
        curMaxHeight = cellSizePixels * rowCount
        canvas.drawRect(
            0F,
            0F,
            width.toFloat(),
            curMaxHeight,
            thickLinePaint
        )

        drawCellsOutlines(canvas)

        fillCells(canvas)
        checkRemoveRow()
    }


    /**
     * Upon start of the game, generates random numbers to fill game with
     *
     * TODO: adjust randomiser to be less repetitive
     */
    private fun fillGameNumberArray() {
        numberCount = Random.nextInt(30, 50)

        for (i in 0 until numberCount) {
            gameNumberArray += Random.nextInt(1, 9)
            gameSolvedArray += false
            gameBlockArray += false
        }
    }

    /**
     * Draws the outline of the cells
     */
    private fun drawCellsOutlines(canvas: Canvas) {
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


    /**
     * Draws the content of the cells; the numbers, the background colors, the lines
     */
    private fun fillCells(canvas: Canvas) {
        var rows: Int = numberCount / numbersPerRow
        if (numberCount % numbersPerRow != 0)
            rows += 1

        if(selectedRow != -1){
            highlightSelectedCell(selectedRow, selectedCol, canvas)
        }

        for (r in 0 until rows){
            for(c in 0 until numbersPerRow) {
                writeNumber(canvas, c, r)
            }
        }

        drawBlockingNumbers(canvas)
        if(matchFound){
            if (typeBasicMatch){
                drawBasicSolutionLine(canvas)
            } else {
                drawOverflowSolutionLine(canvas)
            }
        }
    }

    /**
     * Writes the according number into the cell at position row/col in the correct color
     */
    private fun writeNumber(canvas: Canvas, c: Int, r: Int) {
        if (position(r,c) >= numberCount) {
            return
        }

        if (gameBlockArray[position(r,c)]) {
            return
        }
        val paint: Paint = if (gameSolvedArray[position(r, c)]){
            solvedTextPaint
        } else {
            
            textPaint
        }

        canvas.drawText(
            gameNumberArray[position(r,c)].toString(),
            c * cellSizePixels + (cellSizePixels - fontSize),
            (r + 1) * cellSizePixels - (cellSizePixels - fontSize),
            paint
        )
    }

    /**
     * Highlights the current selected cell(s) in the according number; if first selection, wrong match or correct match.
     * For the latter two, also unselects the cells for next redraw.
     */
    private fun highlightSelectedCell(r: Int, c: Int, canvas: Canvas) {
        if (selectedCol != prevSelectedCol || selectedRow != prevSelectedRow) { // only if selected cell isn't already selected check for matches, else unselect
            when (checkCorrectSelection()) {
                0 -> { // CASE: correct!
                    matchFound = true
                    gameSolvedArray[prevSelectedRow * numbersPerRow + prevSelectedCol] = true
                    gameSolvedArray[selectedRow * numbersPerRow + selectedCol] = true
                    fillCell(canvas, prevSelectedRow, prevSelectedCol, correctSelectionPaint)
                    fillCell(canvas, selectedRow, selectedCol, correctSelectionPaint)
                    rewritePreviousText(canvas, solvedTextPaint)
                    unselectCells()
                }
                1 -> { // CASE: wrong combo
                    matchFound = false
                    fillCell(canvas, selectedRow, selectedCol, wrongSelectionPaint)
                    fillCell(canvas, prevSelectedRow, prevSelectedCol, wrongSelectionPaint)
                    rewritePreviousText(canvas, textPaint)
                    unselectCells()
                }
                2 -> { // CASE: selected first cell
                    matchFound = false
                    fillCell(canvas, r, c, selectedCellPaint)
                }
            }
        } else { // CASE: unselect current cell
            unselectCells()
        }
    }

    /**
     * This highlights the numbers that are blocking a correct match by writing them in red
     */
    private fun drawBlockingNumbers(canvas: Canvas){
        var position = 0
        for (i in gameBlockArray) {
            if (i){
                canvas.drawText(
                    gameNumberArray[position].toString(),
                    (position % numbersPerRow) * cellSizePixels + (cellSizePixels - fontSize), // col
                    ((position / numbersPerRow) + 1) * cellSizePixels - (cellSizePixels - fontSize), // row
                    wrongTextPaint
                )
                gameBlockArray[position] = false
            }
            position++
        }
    }

    /**
     * Sometimes the previously selected cell is drawn over by e.g. the background color, this redraws it
     */
    private fun rewritePreviousText(canvas: Canvas, paint: Paint){
        canvas.drawText(
            gameNumberArray[position(prevSelectedRow,prevSelectedCol)].toString(),
            prevSelectedCol * cellSizePixels + (cellSizePixels - fontSize),
            (prevSelectedRow + 1) * cellSizePixels - (cellSizePixels - fontSize),
            paint
        )
    }

    /**
     * Fills cell with background color
     */
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

    /**
     * Handles all user input
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    /**
     * Hangles click user input
     */
    private fun handleTouchEvent(x: Float, y: Float) {
        prevSelectedCol = selectedCol
        prevSelectedRow = selectedRow
        prevSelectedNr = selectedNr
        selectedCol = (x/cellSizePixels).toInt()
        selectedRow = (y/cellSizePixels).toInt()
        if (checkValidSelectedCell()){
            selectedNr = gameNumberArray[position(selectedRow, selectedCol)]
        } else {
            unselectCells()
        }
        invalidate()

    }

    /**
     * Resets all variables used to store the currently selected cells and their numbers
     */
    private fun unselectCells() {
        selectedCol = -1
        selectedRow = -1
        prevSelectedCol = -1
        prevSelectedRow = -1
        prevSelectedNr = -1
        selectedNr = -1
    }

    /**
     * Checks if selected cell is 'valid' - if it is part of the game or empty
     * returns:
     * - true: clicked cell contains number
     * - false: clicked cell is empty
     */
    private fun checkValidSelectedCell(): Boolean {
        if (position(selectedRow, selectedCol) >= numberCount) return false
        if (selectedRow != -1 && position(selectedRow, selectedCol) != -1 && gameSolvedArray[position(selectedRow, selectedCol)]) return false
        return true
    }

    /**
     * Calculates the position within the arrays based on the row & col position.
     * Returns: the Int position within arrays. If invalid postion, returns -1
     */
    private fun position(row: Int, col: Int): Int{
        return row * numbersPerRow + col
    }



    /**
     * Checks if there are two selected cells and if the two selected cells are a correct & valid combination
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
            if (checkOverrun()) return 0
        } //correct match

        typeBasicMatch = false
        return 1 // wrong match
    }

    /**
     * Draws the line that connects correct solutions in case of horizontal, vertical or diadonal matches.
     */
    private fun drawBasicSolutionLine(canvas: Canvas){
        canvas.drawLine(
            ((prevSelectedCol +0.5F) * cellSizePixels),
            ((prevSelectedRow.toFloat()+0.5F) * cellSizePixels),
            ((selectedCol +0.5F) * cellSizePixels),
            ((selectedRow +0.5F) * cellSizePixels),
            solutionLinePaint)
    }

    /**
     * Draws the line that connects correct solutions in case of 'line overflow' solutions.
     */
    private fun drawOverflowSolutionLine(canvas: Canvas){
        val higherRow = min(selectedRow, prevSelectedRow)
        val lowerRow = max(selectedRow,prevSelectedRow)
        var higherCol: Int
        var lowerCol: Int
        if (higherRow == selectedRow){
            higherCol = selectedCol
            lowerCol = prevSelectedCol
        }
        else {
            higherCol = prevSelectedCol
            lowerCol = selectedCol
        }

        canvas.drawLine(
            ( higherCol +0.5F) * cellSizePixels,
            ( higherRow +0.5F) * cellSizePixels,
            ( numbersPerRow +1) * cellSizePixels,
            ( higherRow +0.5F) * cellSizePixels,
            solutionLinePaint
        )

        canvas.drawLine(
            0F,
            (lowerRow + 0.5F) * cellSizePixels,
            (lowerCol + 0.5F) * cellSizePixels,
            (lowerRow + 0.5F) * cellSizePixels,
            solutionLinePaint
        )
    }

    private fun checkHorizontalMatch(): Boolean {
        if (prevSelectedRow!= selectedRow) return false

        if(checkBetweenHor()) {
            typeBasicMatch = true
            return true
        }
        return false
    }

    private fun checkVerticalMatch(): Boolean {
        if (prevSelectedCol != selectedCol) return false

        if (checkBetweenVer()) {
            typeBasicMatch = true
            return true
        }
        return false
    }

    private fun checkBetweenVer(): Boolean {
        val lowerRow = min(prevSelectedRow, selectedRow) +1
        val higherRow = max(prevSelectedRow, selectedRow)
        var solution = true

        for (i in lowerRow until higherRow) {
            if (!gameSolvedArray[position(i,selectedCol)]) {
                solution= false
                gameBlockArray[position(i,selectedCol)] = true
            }
        }
        return solution
    }

    private fun checkBetweenHor(): Boolean {
        val lowerCol = min(prevSelectedCol, selectedCol) +1
        val higherCol = max(prevSelectedCol, selectedCol)
        var solution = true

        for (i in lowerCol until higherCol) {
            if (!gameSolvedArray[position(selectedRow,i)]) {
                gameBlockArray[position(selectedRow,i)] = true
                solution = false
            }
        }
        return solution
    }

    private fun checkDiagonalMatch(): Boolean {
        val lowerRow = min(prevSelectedRow, selectedRow) +1
        val higherRow = max(prevSelectedRow, selectedRow)
        val lowerCol = min(prevSelectedCol, selectedCol) +1
        val higherCol = max(prevSelectedCol, selectedCol)

        if (lowerRow - higherRow != lowerCol - higherCol)
            return false // not diagonal

        when (checkDiaDirection()) {
            0 -> {
                for( i in 0 until lowerRow - higherRow -1)
                    if (!gameSolvedArray[position(lowerRow +i, lowerCol +i)]) return false
            }
            1 -> {
                for( i in 0 until lowerRow - higherRow -1)
                    if (!gameSolvedArray[position(lowerRow +i, lowerCol -i)]) return false
            }
            else -> {
                print("Error")
            }
        }

        typeBasicMatch = true
        return true
    }

    private fun checkOverrun(): Boolean {
        if (prevSelectedRow - selectedRow == 1){
            for (i in selectedCol+1 until numbersPerRow){
                if (!gameSolvedArray[position(selectedRow,i)]) return false
            }
            for (i in 0 until prevSelectedCol){
                if (!gameSolvedArray[position(prevSelectedRow,i)]) return false
            }
            typeBasicMatch = false
            return true

        } else if (selectedRow - prevSelectedRow == 1) {
            for (i in prevSelectedCol+1 until numbersPerRow){
                if (!gameSolvedArray[position(prevSelectedRow,i)]) return false
            }
            for (i in 0 until selectedCol){
                if (!gameSolvedArray[position(selectedRow,i)]) return false
            }
            typeBasicMatch = false
            return true
        }
        return false
    }

    /**
     * Checks which direction the diagonal match is.
     * return:
     * - 0: up diagonal
     * - 1: down diagonal
     */
    private fun checkDiaDirection(): Int {
        return if (prevSelectedRow - selectedRow < 0 && prevSelectedCol - selectedCol < 0 ||
            prevSelectedRow - selectedRow > 0 && prevSelectedCol - selectedCol > 0)
            0
        else 1
    }

    /**
     * Checks if there needs to be a removed line and calls function to remove it if yes.
     */
    private fun checkRemoveRow() {
        val rows: Int = (numberCount / numbersPerRow)

        for (i in 0..rows) {
            var removeRow = true
            loop@ for (j in 0..numbersPerRow) {
                if (position(i, j) < numberCount)  {
                    if(!gameSolvedArray[position(i, j)]){
                        removeRow = false
                        break@loop
                    }
                }
            }
            if (removeRow) {
                removeRow(i)
            }
        }
    }

    /**
     * Removes the given row
     */
    private fun removeRow(row: Int) {
        for (i in 0..numbersPerRow-1){
            val position = position(row,0)
            if( position < numberCount){
                gameNumberArray.removeAt(position)
                gameBlockArray.removeAt(position)
                gameSolvedArray.removeAt(position)
                numberCount -=1
            }
        }
        checkWin()
    }

    /**
     * Checks if the level was won
     */
    private fun checkWin() {
        if (numberCount == 0){
            Log.d("TAG", "WIN")
        }
    }
}