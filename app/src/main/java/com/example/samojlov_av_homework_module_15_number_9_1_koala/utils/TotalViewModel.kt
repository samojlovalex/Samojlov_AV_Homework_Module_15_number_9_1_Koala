package com.example.samojlov_av_homework_module_15_number_9_1_koala.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {
    var totalSum = "0.00"

    val currentTotalSum: MutableLiveData<String> by lazy { MutableLiveData<String>() }
}