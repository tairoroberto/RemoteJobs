package com.remoteok.io.app.viewmodel.highestpaid

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.remoteok.io.app.domain.highestpaid.HighestPaidUseCase

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