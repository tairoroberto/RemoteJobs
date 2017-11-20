package com.remoteok.io.app.detail.presenter

import android.content.Context
import android.util.Log
import com.remoteok.io.app.detail.contract.DetailContract
import com.remoteok.io.app.detail.model.DetailModel
import com.remoteok.io.app.home.model.domain.Job

/**
 * Created by tairo on 12/12/17.
 */
class DetailPresenter : DetailContract.Presenter {

    private var view: DetailContract.View? = null
    private var model: DetailContract.Model = DetailModel(this)

    override fun attachView(view: DetailContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun manipulateResponse(job: Job?) {
        Log.i("LOG", "job ${job?.position}")

        view?.show(job)
    }

    override fun showError(str: String) {
        view?.showSnackBarError(str)
    }

    override fun getContext(): Context? = view?.getContext()
}