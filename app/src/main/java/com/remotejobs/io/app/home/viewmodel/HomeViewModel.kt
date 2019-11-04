package com.remotejobs.io.app.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotejobs.io.app.home.usecase.HomeUseCase
import com.remotejobs.io.app.model.Job
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync


/**
 * Created by tairo on 12/12/17.
 */
class HomeViewModel(private val homeUseCase: HomeUseCase) : ViewModel() {

    private val disposables = CompositeDisposable()

    val response: MutableLiveData<List<Job>> = MutableLiveData()

    val loadingStatus = MutableLiveData<Boolean>()

    val errorStatus = MutableLiveData<String>()

    private var lastItem: String? = null

    fun getAllJobs() {
        disposables.add(homeUseCase.getJobs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStatus.setValue(true) }
            .doAfterTerminate { loadingStatus.setValue(false) }
            .doOnError { loadResponseFromDataBase(homeUseCase.listJobsFromBD()) }
            .subscribe(
                { jobs ->
                    response.value = jobs.response//?.sortedBy { it.epoch }
                    lastItem = jobs.lastItem
                    doAsync {
                        homeUseCase.deleteAllJobs()
                        homeUseCase.addAllJobs(response.value)
                    }
                },
                { throwable ->
                    errorStatus.value = throwable.message.toString()
                    loadResponseFromDataBase(homeUseCase.listJobsFromBD())
                }
            )
        )
    }

    fun searchJobs(search: String) {
        disposables.add(homeUseCase.searchJobs(search)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStatus.setValue(true) }
            .doAfterTerminate { loadingStatus.setValue(false) }
            .doOnError { loadResponseFromDataBase(homeUseCase.listJobsFromBD()) }
            .subscribe(
                { jobs ->
                    response.value = jobs.response?.sortedBy { it.epoch }
                },
                { throwable ->
                    errorStatus.value = throwable.message.toString()
                    loadResponseFromDataBase(homeUseCase.listJobsFromBD())
                }
            )
        )
    }

    private fun loadResponseFromDataBase(jobsResponse: Flowable<List<Job>>) {
        disposables.add(jobsResponse
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStatus.setValue(true) }
            .doAfterTerminate { loadingStatus.setValue(false) }
            .subscribe(
                { jobs ->
                    response.value = jobs
                },
                { throwable ->
                    errorStatus.value = throwable.message.toString()
                }
            ))
    }
}