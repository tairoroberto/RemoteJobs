package com.remoteok.io.app.home.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.remoteok.io.app.home.model.AppDatabase

/**
 * Created by tairo on 11/30/17 11:26 PM.
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(AppDatabase.getInstance(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}