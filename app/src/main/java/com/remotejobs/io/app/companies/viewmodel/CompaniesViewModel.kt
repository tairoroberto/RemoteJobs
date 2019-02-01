package com.remotejobs.io.app.companies.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotejobs.io.app.companies.usecase.CompaniesUseCase
import com.remotejobs.io.app.model.Company
import com.remotejobs.io.app.model.Job
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class CompaniesViewModel(val companiesUseCase: CompaniesUseCase) : ViewModel() {
    private val disposables = CompositeDisposable()

    val response: MutableLiveData<List<Company>> = MutableLiveData()

    val responseJobs: MutableLiveData<List<Job>> = MutableLiveData()

    val loadingStatus = MutableLiveData<Boolean>()

    val errorStatus = MutableLiveData<String>()

    fun listAllCompanies() {
        disposables.add(companiesUseCase.listAllCompanies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
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
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
                .subscribe(
                        { companies ->
                            response.value = companies
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                        }
                ))
    }

    fun listCompaniesJobs(company: String) {
        disposables.add(companiesUseCase.listCompaniesJobs(company)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
                .doOnError { loadCompaniesJobsFromDataBase(companiesUseCase.listCompaniesJobsFromBD(company)) }
                .subscribe(
                        { response ->
                            responseJobs.value = response.response
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                            loadCompaniesJobsFromDataBase(companiesUseCase.listCompaniesJobsFromBD(company))
                        }
                )
        )
    }

    private fun loadCompaniesJobsFromDataBase(companiesResponse: Flowable<List<Job>>) {
        disposables.add(companiesResponse
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .subscribe(
                        { companies ->
                            responseJobs.value = companies
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                        }
                ))
    }
}