package br.com.tairoroberto.remoteok.home.presenter

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import br.com.tairoroberto.remoteok.home.model.domain.Job
import br.com.tairoroberto.remoteok.home.contract.HomeContract
import br.com.tairoroberto.remoteok.home.model.domain.JobsResponse
import br.com.tairoroberto.remoteok.home.model.AppDatabase
import br.com.tairoroberto.remoteok.home.model.HomeModel
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 12/12/17.
 */
class HomePresenter(application: Application) : AndroidViewModel(application), HomeContract.Presenter {

    private var view: HomeContract.View? = null
    private var model: HomeContract.Model? = null

    override fun attachView(view: HomeContract.View) {
        this.view = view
        this.model = HomeModel(this)
        model = HomeModel(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadJobs() {
        model?.listAll()
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
        model?.search(query)
    }

    override fun loadFromBD() {
        model?.listFromBD()
    }

    override fun getContext(): Context? {
        return view?.getContext()
    }

    override fun getActivity(): Activity? {
        return view?.getActivity()
    }
}