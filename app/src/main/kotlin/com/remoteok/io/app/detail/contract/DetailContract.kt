package com.remoteok.io.app.detail.contract

import android.content.Context
import com.remoteok.io.app.base.BaseMVP
import com.remoteok.io.app.home.model.domain.Job

/**
 * Created by tairo on 8/15/17.
 */
class DetailContract {

    interface Model

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