package com.example.numbermatchreworked

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Observable

class FragmentViewModel: ViewModel() {

    var score = 0
    var highscore = 0


    fun increaseHighscore(points: Int){
        score += points
    }

    fun addNewNumbersClick() {

    }
}