package com.remoteok.io.app.viewmodel.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.remoteok.io.app.domain.HomeUseCase
import com.remoteok.io.app.model.Job
import com.remoteok.io.app.model.JobsResponse
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

    private val response: MutableLiveData<List<Job>> = MutableLiveData()

    private val responseFromDataBase: MutableLiveData<List<Job>> = MutableLiveData()

    private val loadingStatus = MutableLiveData<Boolean>()

    private val errorStatus = MutableLiveData<String>()

    fun getLoadingStatus(): MutableLiveData<Boolean> {
        return loadingStatus
    }

    fun getErrorStatus(): MutableLiveData<String> {
        return errorStatus
    }

    fun getResponse(): MutableLiveData<List<Job>> {
        return response
    }

    fun getResponseFromDataBase(): MutableLiveData<List<Job>> {
        return responseFromDataBase
    }

    fun getAllJobs() {
        loadResponse(homeUseCase.listAllJobs())
    }

    fun search(query: String) {
        loadResponse(homeUseCase.searchJobs(query.toLowerCase()))
    }

    private fun loadResponse(jobsResponse: Flowable<JobsResponse>) {
        disposables.add(jobsResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({ loadingStatus.setValue(true) })
                .doAfterTerminate({ loadingStatus.setValue(false) })
                .doOnError { loadResponseFromDataBase(homeUseCase.listJobsFromBD()) }
                .subscribe(
                        { jobs ->
                            response.value = jobs.list?.subList(0, if (jobs.list?.size as Int > 30) 30 else jobs.list?.lastIndex as Int)
                            doAsync {
                                homeUseCase.deleteAllJobs()
                                homeUseCase.addAllJobs(response.value)
                            }
                        },
                        {
                            throwable -> errorStatus.value = throwable.message.toString()
                            loadResponseFromDataBase(homeUseCase.listJobsFromBD())
                        }
                )
        )
    }

    private fun loadResponseFromDataBase(jobsResponse: LiveData<List<Job>>) {
        doAsync {
            jobsResponse.observeForever {
                if (it?.isNotEmpty() == true) {
                    responseFromDataBase.value = it.subList(0, if (it.size > 30) 30 else it.lastIndex).sortedByDescending { it.id }
                }
            }
        }
    }
}