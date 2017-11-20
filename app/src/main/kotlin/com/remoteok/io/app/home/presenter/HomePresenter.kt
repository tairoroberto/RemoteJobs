package com.remoteok.io.app.home.presenter

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import com.remoteok.io.app.home.model.domain.Job
import com.remoteok.io.app.home.contract.HomeContract
import com.remoteok.io.app.home.model.domain.JobsResponse
import com.remoteok.io.app.home.model.AppDatabase
import com.remoteok.io.app.home.model.HomeModel
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 12/12/17.
 */
class HomePresenter(application: Application) : AndroidViewModel(application), HomeContract.Presenter {

    private var view: HomeContract.View? = null
    private var model: HomeContract.Model = HomeModel(this)

    override fun attachView(view: HomeContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadJobs() {
        model.listAll()
    }

    override fun manipulateResponse(jobsResponse: JobsResponse) {
        view?.getActivity()?.doAsync {
            AppDatabase.getInstance(view?.getContext()).jobsDAO().insertAll(jobsResponse.list)
        }

        view?.showJobsList(jobsResponse.list)
    }

    override fun manipulateResponseDB(jobs: List<Job>?) {
        view?.showJobsList(jobs)
    }

    override fun showError(str: String) {
        view?.showSnackBarError(str)
        view?.showProgress(false)
    }

    override fun search(query: String) {
        model.search(query.toLowerCase())
    }

    override fun loadFromBD() {
        model.listFromBD()
    }

    override fun getContext(): Context? = view?.getContext()

    override fun getActivity(): Activity? = view?.getActivity()
}