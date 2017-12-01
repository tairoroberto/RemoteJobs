package com.remoteok.io.app.home.model

import android.arch.lifecycle.LiveData
import com.remoteok.io.app.CustomApplication.Companion.context
import com.remoteok.io.app.base.api.Api
import com.remoteok.io.app.home.model.domain.Job
import io.reactivex.Flowable

/**
 * Created by tairo on 12/11/17.
 */
class HomeModel {
    fun listAll(): Flowable<List<Job>> {
        return Api(context).getApiService().getAll()
    }

    fun search(query: String): Flowable<List<Job>> {
        return Api(context).getApiService().search(query)
    }

    fun listFromBD(): LiveData<List<Job>> {
        return AppDatabase.getInstance(context).jobsDAO().getAll()
    }
}
