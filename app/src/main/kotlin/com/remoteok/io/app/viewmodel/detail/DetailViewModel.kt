package com.remoteok.io.app.viewmodel.detail

import android.arch.lifecycle.ViewModel
import com.remoteok.io.app.model.Job
import timber.log.Timber

/**
 * Created by tairo on 12/12/17.
 */
class DetailViewModel : ViewModel() {

    fun manipulateResponse(job: Job?) {
        Timber.i("job ${job?.position}")

    }

    fun showError(str: String) {

    }
}