package com.example.numbermatchreworked


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import kotlin.math.max
import kotlin.math.min


private var cellSizePixels = 0F
private var cellsPerRow = 9
private var rowCount = 10
private var boardUsedHeight = 0F
private var boardUsedWidth = 0F



@Composable
fun GameCanvas(gLogic: GameLogic) {
    val textMeasurer = rememberTextMeasurer()
    var invalidations by remember{ mutableIntStateOf(0) }


    fun DrawScope.calculateBoardVariables() {
        //TODO: Adapt for broad screens
        cellSizePixels = size.width / cellsPerRow
        boardUsedHeight = rowCount * cellSizePixels
    }


    fun DrawScope.fillCell(row: Int, col: Int, color: Color) {
        this.drawRect(
            topLeft = Offset(col*cellSizePixels, row*cellSizePixels),
            size = Size(cellSizePixels,cellSizePixels),
            color = color)

    }


    fun DrawScope.drawHighlights(){
        when(gLogic.selection){
            "none" -> {}
            "first" -> {
                this.fillCell(gLogic.selectedRow, gLogic.selectedCol, selectionColor)
            }
            "correct" -> {
                this.fillCell(gLogic.selectedRow, gLogic.selectedCol, correctionSelectionColor)
                this.fillCell(gLogic.prevSelectedRow, gLogic.prevSelectedCol, correctionSelectionColor)
            }
            "wrong" -> {
                this.fillCell(gLogic.selectedRow, gLogic.selectedCol, wrongSelectionColor)
                this.fillCell(gLogic.prevSelectedRow, gLogic.prevSelectedCol, wrongSelectionColor)
            }
            "hint" -> {
                for (i in gLogic.hintHighlightArray){
                    this.fillCell(i/cellsPerRow,i % cellsPerRow, hintColor )
                }
            }
        }
    }


    fun DrawScope.drawGrid() {
        for (i in 0 until cellsPerRow +1) {
            this.drawLine(
                start = Offset(i * cellSizePixels, 0F),
                end = Offset(i * cellSizePixels, boardUsedHeight),
                color = Color.Black,
                strokeWidth = 3F
            )
        }
        for (i in 0 until rowCount + 1) {
            this.drawLine(
                start = Offset(0F, i * cellSizePixels),
                end = Offset(size.width, i*cellSizePixels),
                color = Color.Black,
                strokeWidth = 3F
            )
        }
    }


    fun DrawScope.writeNumber(c: Int, r: Int) {
        if (position(r,c) >= gLogic.numberCount) {
            return
        }
        val textStyle: TextStyle = if (gLogic.gameSolvedArray[position(r, c)]){
            solvedTextStyle
        } else {
            defaultTextStyle
        }

        this.drawText(
            textMeasurer = textMeasurer,
            text = gLogic.gameNumberArray[position(r,c)].toString(),
            topLeft = Offset( //TODO make it make sense
                c*cellSizePixels +cellSizePixels/3,
                r*cellSizePixels +cellSizePixels/5),
            style = textStyle
        )
    }

    fun DrawScope.drawNumbers() {
        val rows: Int = gLogic.numberCount / cellsPerRow +1
        for (r in 0 until rows){
            for(c in 0 until cellsPerRow) {
                writeNumber(c, r)
            }
        }
    }

    fun DrawScope.drawSelectionLines(){
        if (gLogic.selection != "correct") return
        if (gLogic.typeBasicMatch){
            this.drawLine(
                start = Offset(
                    (gLogic.prevSelectedCol + 0.5F) * cellSizePixels,
                    (gLogic.prevSelectedRow.toFloat() + 0.5F) * cellSizePixels
                ),
                end = Offset(
                    (gLogic.selectedCol + 0.5F) * cellSizePixels,
                    (gLogic.selectedRow + 0.5F) * cellSizePixels
                ),
                strokeWidth = 10F,
                color = selectionColor
            )
        } else {
            val higherRow = max(gLogic.selectedRow, gLogic.prevSelectedRow)
            val lowerRow = min(gLogic.selectedRow, gLogic.prevSelectedRow)
            var higherCol: Int
            var lowerCol: Int
            if (higherRow == gLogic.selectedRow){
                higherCol = gLogic.selectedCol
                lowerCol = gLogic.prevSelectedCol
            } else {
                higherCol = gLogic.prevSelectedCol
                lowerCol = gLogic.selectedCol
            }

            this.drawLine(
                start = Offset(
                    (lowerCol + 0.5F) * cellSizePixels,
                    (lowerRow + 0.5F) * cellSizePixels
                ),
                end = Offset(
                    (cellsPerRow * cellSizePixels),
                    (lowerRow + 0.5F) * cellSizePixels
                ),
                strokeWidth = 10F,
                color = selectionColor
            )
            this.drawLine(
                start = Offset(
                    (higherCol + 0.5F) * cellSizePixels,
                    (higherRow + 0.5F) * cellSizePixels
                ),
                end = Offset(
                    0F,
                    (higherRow + 0.5F) * cellSizePixels
                ),
                strokeWidth = 10F,
                color = selectionColor
            )
        }
    }

    //patheffect?

    fun DrawScope.redrawBoard(){
        drawHighlights()
        drawGrid()
        drawNumbers()
        drawSelectionLines()

        gLogic.checkRemoveRow()
    }



    Canvas(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.875F)
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { tapOffset ->
                    gLogic.onCLick(tapOffset, cellSizePixels)
                    invalidations +=1
                }
            )
        }
    ) {
        if(gLogic.gameNumberArray.isEmpty()){
            calculateBoardVariables()
            gLogic.startNewLevel()
        }

        redrawBoard()
        invalidations.let{ inv->
            redrawBoard()
        }

    }
}



fun position(row: Int, col: Int): Int{
    return row * cellsPerRow + col
}
