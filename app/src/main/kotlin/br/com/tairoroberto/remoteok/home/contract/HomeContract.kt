package br.com.tairoroberto.remoteok.home.contract

import android.app.Activity
import android.content.Context
import br.com.tairoroberto.remoteok.home.model.domain.Job
import br.com.tairoroberto.remoteok.base.BaseMVP
import br.com.tairoroberto.remoteok.home.model.domain.JobsResponse

/**
 * Created by tairo on 12/12/17.
 */
class HomeContract {

    interface Model {
        fun listAll()
        fun search(query: String)
        fun listFromBD()
    }

    interface View : BaseMVP.View {
        fun showJobsList(jobs: List<Job>?)
        fun showProgress(b: Boolean)
        fun showSnackBarError(str: String)
    }

    interface Presenter : BaseMVP.Presenter<View> {
        fun loadJobs()
        fun manipulateResponse(jobsResponse: JobsResponse)
        fun manipulateResponseDB(jobs: List<Job>?)
        fun showError(str: String)
        fun search(query: String)
        fun loadFromBD()
        fun getContext(): Context?
        fun getActivity(): Activity?
    }
}