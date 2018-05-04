package com.remotejobs.io.app.viewmodel.highestpaid

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remotejobs.io.app.domain.highestpaid.HighestPaidUseCase
import com.remotejobs.io.app.model.HighestPaid
import com.remotejobs.io.app.model.Job
import io.reactivex.Flowable
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

    private val responseJobs: MutableLiveData<List<Job>> = MutableLiveData()

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

    fun getHighestPaidJobsResponse(): MutableLiveData<List<Job>> {
        return responseJobs
    }

    fun getAllHighestPaidSalaries() {
        disposables.add(highestPaidUseCase.getAllHighestPaidSalaries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .doOnError { loadHighestPaidFromDataBase(highestPaidUseCase.listAllHighestPaidSalariesFromBD()) }
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
                            loadHighestPaidFromDataBase(highestPaidUseCase.listAllHighestPaidSalariesFromBD())
                        }
                )
        )
    }

    private fun loadHighestPaidFromDataBase(highestPaidResponse: Flowable<List<HighestPaid>>) {
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

    fun loadHighestPaidJobs(tag: String) {
        disposables.add(highestPaidUseCase.getAllHighestPaidJobs("remote-$tag-jobs")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .doOnError { loadHighestPaidJobsFromDataBase(highestPaidUseCase.listAllHighestPaidJobsFromBD(tag)) }
                .subscribe(
                        { jobs ->
                            responseJobs.value = jobs.list
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                            loadHighestPaidJobsFromDataBase(highestPaidUseCase.listAllHighestPaidJobsFromBD(tag))
                        }
                )
        )
    }

    private fun loadHighestPaidJobsFromDataBase(jobsResponse: Flowable<List<Job>>) {
        disposables.add(jobsResponse
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .subscribe(
                        { jobs ->
                            responseJobs.value = jobs
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                        }
                ))
    }
}