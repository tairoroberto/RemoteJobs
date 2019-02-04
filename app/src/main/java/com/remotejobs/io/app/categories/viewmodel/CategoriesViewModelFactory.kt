package com.remotejobs.io.app.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remotejobs.io.app.categories.usecase.CategoriesUseCase

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class CategoriesViewModelFactory(private val categoriesUseCase: CategoriesUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(categoriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}