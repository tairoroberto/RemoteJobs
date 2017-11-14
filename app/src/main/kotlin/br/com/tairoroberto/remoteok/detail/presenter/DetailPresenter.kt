package br.com.tairoroberto.remoteok.detail.presenter

import android.content.Context
import android.util.Log
import br.com.tairoroberto.remoteok.detail.contract.DetailContract
import br.com.tairoroberto.remoteok.detail.model.DetailModel
import br.com.tairoroberto.remoteok.home.model.domain.Job

/**
 * Created by tairo on 12/12/17.
 */
class DetailPresenter : DetailContract.Presenter {

    private var view: DetailContract.View? = null
    private var model: DetailContract.Model? = null

    override fun attachView(view: DetailContract.View) {
        this.view = view
        this.model = DetailModel(this)
    }

    override fun detachView() {
        this.view = null
    }

    override fun getById(id: String) {
        model?.searchMovieById(id)
    }

    override fun manipulateResponse(job: Job?) {
        Log.i("LOG", "movie ${job?.position}")

        view?.show(job)
    }

    override fun showError(str: String) {
        view?.showSnackBarError(str)
    }

    override fun getContext(): Context? {
        return view?.getContext()
    }
}