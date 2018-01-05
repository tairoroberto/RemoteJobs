package com.remoteok.io.app.home.model

import android.arch.lifecycle.LiveData
import com.remoteok.io.app.CustomApplication.Companion.context
import com.remoteok.io.app.base.api.Api
import com.remoteok.io.app.home.model.domain.Job
import com.remoteok.io.app.home.model.domain.JobsResponse
import io.reactivex.Flowable

/**
 * Created by tairo on 12/11/17.
 */
class HomeModel {
    fun listAll(): Flowable<JobsResponse> {
        return this.search("remote-jobs")
    }

    fun search(query: String): Flowable<JobsResponse> {
        return Api(context).getApiService().search(query)
    }

    fun listFromBD(): LiveData<List<Job>> {
        return AppDatabase.getInstance(context).jobsDAO().getAll()
    }
}
