package com.remotejobs.io.app.highestpaid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remotejobs.io.app.highestpaid.domain.HighestPaidUseCase

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class HighestPaidViewModelFactory(private val highestPaidUseCase: HighestPaidUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HighestPaidViewModel::class.java)) {
            return HighestPaidViewModel(highestPaidUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}