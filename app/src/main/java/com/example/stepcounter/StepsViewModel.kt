package com.example.stepcounter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepsViewModel : ViewModel() {
    var totalSteps : MutableLiveData<Float> = MutableLiveData()

    fun setTotalSteps(liveDataValue: Float) {
        totalSteps.value = liveDataValue
    }

    val previousTotalSteps: MutableLiveData<Float> = MutableLiveData()

    fun setPreviousTotalSteps(liveDataValue: Float) {
        previousTotalSteps.value = liveDataValue
    }

    val currentSteps: MutableLiveData<Float> = MutableLiveData()

    fun setCurrentSteps(liveDataValue: Float) {
        currentSteps.value = liveDataValue
    }
}