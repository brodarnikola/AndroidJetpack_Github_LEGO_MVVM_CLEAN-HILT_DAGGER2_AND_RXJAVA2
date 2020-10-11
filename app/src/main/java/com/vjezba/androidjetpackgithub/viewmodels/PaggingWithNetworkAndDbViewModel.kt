package com.vjezba.androidjetpackgithub.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaggingWithNetworkAndDbViewModel(
) : ViewModel() {


    private val _incrementNumberAutomaticallyByOne = MutableLiveData<Int>().apply {
        value = 0
    }

    val incrementNumberAutomaticallyByOne: LiveData<Int> = _incrementNumberAutomaticallyByOne

    fun incrementAutomaticallyByOne() {
        Log.d("TestVM", "The automatic amount is being increment, current value = ${_incrementNumberAutomaticallyByOne.value}")
        _incrementNumberAutomaticallyByOne.value?.let { number ->
            _incrementNumberAutomaticallyByOne.value = number + 1
        }
    }



    private val _incrementNumberManuallyyByOne = MutableLiveData<Int>().apply {
        value = 0
    }

    val incrementNumberManuallyByOne: LiveData<Int> = _incrementNumberManuallyyByOne

    fun incrementManuallyByOne() {
        Log.d("TestVM", "The manually amount is being increment, current value = ${_incrementNumberManuallyyByOne.value}")
        _incrementNumberManuallyyByOne.value?.let { number ->
            _incrementNumberManuallyyByOne.value = number + 1
        }
    }

}
