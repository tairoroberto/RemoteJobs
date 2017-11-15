package br.com.tairoroberto.remoteok.detail.contract

import android.content.Context
import br.com.tairoroberto.remoteok.base.BaseMVP
import br.com.tairoroberto.remoteok.home.model.domain.Job

/**
 * Created by tairo on 8/15/17.
 */
class DetailContract {

    interface Model {

    }

    interface View : BaseMVP.View {
        fun show(job: Job?)
        fun showSnackBarError(msg: String)
    }

    interface Presenter : BaseMVP.Presenter<View> {
        fun manipulateResponse(job: Job?)
        fun showError(str: String)
        fun getContext(): Context?
    }
}