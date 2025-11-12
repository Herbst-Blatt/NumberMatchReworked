package com.example.numbermatchreworked

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


class GameLogic: ViewModel() {

    private val _points = MutableLiveData(0)
    val points: LiveData<Int> = _points
    //var points = 0

    //private var _invalidations = MutableStateFlow(0)
    //var logicInvalidations = _invalidations.asStateFlow()
    var level = 0

    // var points = 0
    var numberCount = -1
    var gameNumberArray = arrayListOf<Int>()
    var gameSolvedArray = arrayListOf<Boolean>()
    var gameBlockArray = arrayListOf<Boolean>()

    var hintHighlightArray = arrayListOf<Int>()

    var selectedCol = -1
    var selectedRow = -1
    var selectedNr = -1

    var prevSelectedCol = -1
    var prevSelectedRow = -1
    var prevSelectedNr = -1


    //var matchFound = false
    var typeBasicMatch = true
    private var cellsPerRow = 9 // duplicate, that's bad


    var selection = "none" // "none" "first" "correct" "wrong" "hint"// TODO

    fun startNewLevel() {
        level += 1
        numberCount = Random.nextInt(30, 50)


        for (i in 0 until numberCount) {
            gameSolvedArray += false
            gameBlockArray += false
            addRandomToNumberArray(i)
        }
    }

    /**
     * Adds one random number to the number array; makes repeating and fitting numbers less likely
     */
    private fun addRandomToNumberArray(arrayPos: Int) {
        var deleteMe = gameNumberArray
        var nextRand = Random.nextInt(1, 10)
        var generateNewRand = false
        while(true){
            if (getValidNumber(arrayPos -1) != -1){
                if (nextRand == gameNumberArray[arrayPos- 1] || nextRand + gameNumberArray[arrayPos-1] == 10){
                    if(Random.nextInt(1, 100) > 5){ // if a match exists, it should likely  be generated new
                        generateNewRand = true
                    }
                }
            }

            if(getValidNumber(arrayPos - cellsPerRow) != -1){
                if (nextRand == gameNumberArray[arrayPos - cellsPerRow] || nextRand + gameNumberArray[arrayPos - cellsPerRow] == 10){
                    if(Random.nextInt(1, 100) > 5){ //
                        generateNewRand = true
                    }
                }
            }


            if(getValidNumber(arrayPos - cellsPerRow -1) != -1){
                if (nextRand == gameNumberArray[arrayPos - cellsPerRow -1] || nextRand + gameNumberArray[arrayPos - cellsPerRow -1] == 10){
                    if(Random.nextInt(1, 100) > 5){ //
                        generateNewRand = true
                    }
                }
            }

            if(getValidNumber(arrayPos - cellsPerRow +1) != -1){
                if (nextRand == gameNumberArray[arrayPos - cellsPerRow +1] || nextRand + gameNumberArray[arrayPos - cellsPerRow +1] == 10){
                    if(Random.nextInt(1, 100) > 5){ //
                        generateNewRand = true
                    }
                }
            }

            if(generateNewRand){
                nextRand = Random.nextInt(1, 10)
                generateNewRand = false
            } else {
                gameNumberArray += nextRand
                break
            }
        }
    }


    fun onCLick(offset: Offset, cellSizePixels: Float) {
        if (selection == "correct" || selection == "wrong") {
            unselectCells()
            selection = "none"
        }

        prevSelectedCol = selectedCol
        prevSelectedRow = selectedRow
        prevSelectedNr = selectedNr
        selectedCol = (offset.x / cellSizePixels).toInt()
        selectedRow = (offset.y / cellSizePixels).toInt()



        if (checkValidSelectedCell()) {
            selectedNr = gameNumberArray[position(selectedRow, selectedCol)]
            determineCellHighlights()
        } else {
            selection = "none"
            unselectCells()
        }
    }

    /**
     * Checks if selected cell is 'valid' - if it is part of the game or empty
     * returns:
     * - true: clicked cell contains number
     * - false: clicked cell is empty
     */
    private fun checkValidSelectedCell(): Boolean {
        if (position(selectedRow, selectedCol) >= numberCount) return false
        if (selectedRow != -1 && position(
                selectedRow,
                selectedCol
            ) != -1 && gameSolvedArray[position(selectedRow, selectedCol)]
        ) return false
        return true
    }

    /**
     * Resets all variables used to store the currently selected cells and their numbers
     */
    fun unselectCells() {
        selectedCol = -1
        selectedRow = -1
        prevSelectedCol = -1
        prevSelectedRow = -1
        prevSelectedNr = -1
        selectedNr = -1
    }

    private fun determineCellHighlights() {
        if (selectedCol == prevSelectedCol && selectedRow == prevSelectedRow) {
            selection = "none"
            unselectCells()// if cell is already selected, unselect
            return
        }
        checkCorrectSelection()

        if (selection == "correct") {
            gameSolvedArray[prevSelectedRow * cellsPerRow + prevSelectedCol] = true
            gameSolvedArray[selectedRow * cellsPerRow + selectedCol] = true

            if (typeBasicMatch) {
                _points.postValue(_points.value?.plus(1 * level))
            } else {
                _points.postValue(_points.value?.plus(1 * level))
            }
        }
    }


    /**
     * Checks if there are two selected cells and if the two selected cells are a correct & valid combination
     * Updates 'selection' variable accordingly
     */
    private fun checkCorrectSelection() {
        if (prevSelectedNr == -1) {
            selection = "first"; return
        }

        if (prevSelectedNr == selectedNr || prevSelectedNr + selectedNr == 10) {
            if (checkDiagonalMatch()) {
                selection = "correct"; return
            }
            if (checkHorizontalMatch()) {
                selection = "correct"; return
            }
            if (checkVerticalMatch()) {
                selection = "correct"; return
            }
            if (checkOverrun()) {
                selection = "correct"; return
            }
        }

        typeBasicMatch = false
        selection = "wrong"
    }

    private fun checkHorizontalMatch(): Boolean {
        if (prevSelectedRow != selectedRow) return false

        if (checkBetweenHor()) {
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
        val lowerRow = min(prevSelectedRow, selectedRow) + 1
        val higherRow = max(prevSelectedRow, selectedRow)
        var solution = true

        for (i in lowerRow until higherRow) {
            if (!gameSolvedArray[position(i, selectedCol)]) {
                solution = false
                gameBlockArray[position(i, selectedCol)] = true
            }
        }
        return solution
    }

    private fun checkBetweenHor(): Boolean {
        val lowerCol = min(prevSelectedCol, selectedCol) + 1
        val higherCol = max(prevSelectedCol, selectedCol)
        var solution = true

        for (i in lowerCol until higherCol) {
            if (!gameSolvedArray[position(selectedRow, i)]) {
                gameBlockArray[position(selectedRow, i)] = true
                solution = false
            }
        }
        return solution
    }

    private fun checkDiagonalMatch(): Boolean {
        val lowerRow = min(prevSelectedRow, selectedRow) + 1
        val higherRow = max(prevSelectedRow, selectedRow)
        val lowerCol = min(prevSelectedCol, selectedCol) + 1
        val higherCol = max(prevSelectedCol, selectedCol)

        if (lowerRow - higherRow != lowerCol - higherCol)
            return false // not diagonal

        when (checkDiaDirection()) {
            0 -> {
                for (i in 0 until higherRow - lowerRow) {
                    if (!gameSolvedArray[position(lowerRow + i, lowerCol + i)]) return false
                }

            }

            1 -> {
                for (i in 0 until higherRow - lowerRow)
                    if (!gameSolvedArray[position(lowerRow + i, lowerCol - i)]) return false
            }

            else -> {
                print("Error")
            }
        }

        typeBasicMatch = true
        return true
    }

    /**
     * Checks which direction the diagonal match is.
     * return:
     * - 0: up diagonal
     * - 1: down diagonal
     */
    private fun checkDiaDirection(): Int {
        return if (prevSelectedRow - selectedRow < 0 && prevSelectedCol - selectedCol < 0 ||
            prevSelectedRow - selectedRow > 0 && prevSelectedCol - selectedCol > 0
        )
            0
        else 1
    }

    private fun checkOverrun(): Boolean {
        if (prevSelectedRow - selectedRow == 1) {
            for (i in selectedCol + 1 until cellsPerRow) {
                if (!gameSolvedArray[position(selectedRow, i)]) return false
            }
            for (i in 0 until prevSelectedCol) {
                if (!gameSolvedArray[position(prevSelectedRow, i)]) return false
            }
            typeBasicMatch = false
            return true

        } else if (selectedRow - prevSelectedRow == 1) {
            for (i in prevSelectedCol + 1 until cellsPerRow) {
                if (!gameSolvedArray[position(prevSelectedRow, i)]) return false
            }
            for (i in 0 until selectedCol) {
                if (!gameSolvedArray[position(selectedRow, i)]) return false
            }
            typeBasicMatch = false
            return true
        }
        return false
    }

    public fun addMoreButtonClick() {
        var countAddedNumbers = 0
        for (i in 0..numberCount-1) {
            if (!gameSolvedArray[i]) {
                gameNumberArray += gameNumberArray[i]
                gameSolvedArray += false
                gameBlockArray += false
                countAddedNumbers+=1
            }
        }
        numberCount += countAddedNumbers
        //_invalidations.value = _invalidations.value +1
    }

    public fun getHintsButtonClick(): IntArray {
        for (i in gameNumberArray) {
            if (!gameSolvedArray[i]) {
                val posRight = checkToRight(i)
                if (checkPositions(i, posRight)) return intArrayOf(i, posRight) // right
                val posDown = checkDown(i)
                if (checkPositions(i, posDown)) return intArrayOf(i, posDown) // down
                if (checkPositions(i, i + 1 + cellsPerRow)) return intArrayOf(i, i + 1 + cellsPerRow) // right down

                //left down
            }
        }
        return intArrayOf(-1, -1)
    }

    private fun checkToRight(position:Int): Int {
        var pos = position
        while(pos < numberCount && gameSolvedArray[pos]){
            pos+=1
        }
        return pos
    }

    private fun checkDown(position: Int): Int {
        var pos = position
        while(pos < numberCount && gameSolvedArray[pos]){
            pos+= cellsPerRow
        }
        return pos
    }
    //private fun checkDiaRight(position: Int): Int {

    //}

    private fun getValidNumber(pos: Int): Int {
        if (pos >= numberCount || pos < 0) return -1
        return gameNumberArray[pos]
    }

    private fun checkPositions(a: Int, b: Int): Boolean {
        return checkMatchingNumbers(
            getValidNumber(a),
            getValidNumber(b)
        )
    }

    private fun checkMatchingNumbers(a: Int, b: Int): Boolean {
        if (a == -1 || b == -1) return false
        if (a == b || a + b == 10) return true
        return false
    }

    /**
     * Checks if there needs to be a removed line and calls function to remove it if yes.
     */
    fun checkRemoveRow() {
        val rows: Int = (numberCount / cellsPerRow)

        for (i in 0..rows) {
            var removeRow = true
            loop@ for (j in 0..cellsPerRow) {
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
     * Removes the given Row
     */
    private fun removeRow(row: Int) {
        for (i in 0..cellsPerRow-1){
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

    private fun checkWin() {
        if (numberCount == 0){

        }
    }
}




