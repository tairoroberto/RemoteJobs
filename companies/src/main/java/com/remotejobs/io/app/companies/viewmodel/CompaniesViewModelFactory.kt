package com.remotejobs.io.app.companies.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.remotejobs.io.app.companies.domain.CompaniesUseCase

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class CompaniesViewModelFactory(private val companiesUseCase: CompaniesUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompaniesViewModel::class.java)) {
            return CompaniesViewModel(companiesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}