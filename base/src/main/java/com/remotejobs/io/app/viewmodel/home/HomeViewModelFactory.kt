package com.remotejobs.io.app.viewmodel.home

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.remotejobs.io.app.domain.home.HomeUseCase

/**
 * Created by tairo on 11/30/17 11:26 PM.
 */
class HomeViewModelFactory(private val homeUseCase: HomeUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(homeUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}