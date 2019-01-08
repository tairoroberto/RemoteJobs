package com.remotejobs.io.app.viewmodel.detail

import androidx.lifecycle.ViewModel
import android.util.Log
import com.remotejobs.io.app.model.Job

/**
 * Created by tairo on 12/12/17.
 */
class DetailViewModel : ViewModel() {

    fun manipulateResponse(job: Job?) {
        Log.i("TAG", "job ${job?.position}")

    }

    fun showError(str: String) {

    }
}