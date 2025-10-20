package com.example.numbermatchreworked

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Board_ViewModel: ViewModel() {
    var points = MutableLiveData<Int>(0)

    fun increasePoints(addedPoints: Int){
        points.value?.plus(addedPoints)
    }
}