package com.remotejobs.io.app.viewmodel.companies

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remotejobs.io.app.domain.companies.CompaniesUseCase
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

    private val response: MutableLiveData<List<Company>> = MutableLiveData()

    private val responseJobs: MutableLiveData<List<Job>> = MutableLiveData()

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

    fun getCompanyJobsResponse(): MutableLiveData<List<Job>> {
        return responseJobs
    }

    fun listAllCompanies() {
        disposables.add(companiesUseCase.listAllCompanies()
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

    fun listCompaniesJobs(company: String) {
        disposables.add(companiesUseCase.listCompaniesJobs(company)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .doOnError { loadCompaniesJobsFromDataBase(companiesUseCase.listCompaniesJobsFromBD(company)) }
                .subscribe(
                        { response ->
                            responseJobs.value = response.list
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