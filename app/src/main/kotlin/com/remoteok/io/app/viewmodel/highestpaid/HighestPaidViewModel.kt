package com.remoteok.io.app.viewmodel.highestpaid

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remoteok.io.app.domain.highestpaid.HighestPaidUseCase
import com.remoteok.io.app.model.CompaniesResponse
import com.remoteok.io.app.model.Company
import com.remoteok.io.app.model.HighestPaid
import com.remoteok.io.app.model.HighestPaidRespose
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class HighestPaidViewModel(val highestPaidUseCase: HighestPaidUseCase) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val response: MutableLiveData<List<HighestPaid>> = MutableLiveData()

    private val loadingStatus = MutableLiveData<Boolean>()

    private val errorStatus = MutableLiveData<String>()

    fun getLoadingStatus(): MutableLiveData<Boolean> {
        return loadingStatus
    }

    fun getErrorStatus(): MutableLiveData<String> {
        return errorStatus
    }

    fun getResponse(): MutableLiveData<List<HighestPaid>> {
        return response
    }

    fun getAllHighestPaidSalaries() {
        loadResponse(highestPaidUseCase.getAllHighestPaidSalaries())
    }

    private fun loadResponse(companiesResponse: Single<HighestPaidRespose>) {
        disposables.add(companiesResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .doOnError { loadResponseFromDataBase(highestPaidUseCase.listAllHighestPaidSalariesFromBD()) }
                .subscribe(
                        { companies ->
                            response.value = companies.items
                            doAsync {
                                highestPaidUseCase.deleteAllHighestPaidSalaries()
                                highestPaidUseCase.addAllHighestPaidSalaries(response.value)
                            }
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                            loadResponseFromDataBase(highestPaidUseCase.listAllHighestPaidSalariesFromBD())
                        }
                )
        )
    }

    private fun loadResponseFromDataBase(highestPaidResponse: Flowable<List<HighestPaid>>) {
        disposables.add(highestPaidResponse
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