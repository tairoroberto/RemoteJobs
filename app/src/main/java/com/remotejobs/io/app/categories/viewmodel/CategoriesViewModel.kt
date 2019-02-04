package com.remotejobs.io.app.categories.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotejobs.io.app.categories.usecase.CategoriesUseCase
import com.remotejobs.io.app.model.Job
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by tairo on 2/24/18 11:04 PM.
 */
class CategoriesViewModel(val categoriesUseCase: CategoriesUseCase) : ViewModel() {
    private val disposables = CompositeDisposable()

    val response: MutableLiveData<List<String>> = MutableLiveData()

    val responseJobs: MutableLiveData<List<Job>> = MutableLiveData()

    val loadingStatus = MutableLiveData<Boolean>()

    val errorStatus = MutableLiveData<String>()

    var lastItem: String? = null

    fun getCategories() {
        disposables.add(categoriesUseCase.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
                .doOnError { errorStatus.value = it.message.toString() }
                .subscribe(
                        { categories ->
                            response.value = categories.response
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                        }
                )
        )
    }

    fun getJobsByCategory(tag: String) {
        disposables.add(categoriesUseCase.getJobsByCategory(tag, lastItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
                .doOnError { errorStatus.value = it.message.toString() }
                .subscribe(
                        { jobs ->
                            responseJobs.value = jobs.response
                            lastItem = jobs.lastItem
                        },
                        { throwable ->
                            errorStatus.value = throwable.message.toString()
                        }
                )
        )
    }
}