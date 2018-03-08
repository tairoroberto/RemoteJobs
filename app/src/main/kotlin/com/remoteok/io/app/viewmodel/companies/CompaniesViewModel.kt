package com.remoteok.io.app.viewmodel.companies

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remoteok.io.app.domain.companies.CompaniesUseCase
import com.remoteok.io.app.model.CompaniesResponse
import com.remoteok.io.app.model.Company
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class CompaniesViewModel(val companiesUseCase: CompaniesUseCase) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val response: MutableLiveData<List<Company>> = MutableLiveData()

    private val loadingStatus = MutableLiveData<Boolean>()

    private val errorStatus = MutableLiveData<String>()

    fun getLoadingStatus(): MutableLiveData<Boolean> {
        return loadingStatus
    }

    fun getErrorStatus(): MutableLiveData<String> {
        return errorStatus
    }

    fun getResponse(): MutableLiveData<List<Company>> {
        return response
    }

    fun listAllCompanies() {
        loadResponse(companiesUseCase.listAllCompanies())
    }

    private fun loadResponse(companiesResponse: Single<CompaniesResponse>) {
        disposables.add(companiesResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .doOnError { loadResponseFromDataBase(companiesUseCase.listCompaniesFromBD()) }
                .subscribe(
                        { companies ->
                            response.value = companies.items
                            doAsync {
                                companiesUseCase.deleteAllCompanies()
                                companiesUseCase.addAllCompanies(response.value)
                            }
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                            loadResponseFromDataBase(companiesUseCase.listCompaniesFromBD())
                        }
                )
        )
    }

    private fun loadResponseFromDataBase(companiesResponse: Flowable<List<Company>>) {
        disposables.add(companiesResponse
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .subscribe(
                        { companies ->
                            response.value = companies
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                        }
                ))
    }
}