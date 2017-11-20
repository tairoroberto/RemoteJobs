package com.remoteok.io.app.home.model

import android.util.Log
import com.remoteok.io.app.base.api.Api
import com.remoteok.io.app.home.contract.HomeContract
import com.remoteok.io.app.home.model.domain.JobsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 12/11/17.
 */
class HomeModel(private val presenter: HomeContract.Presenter) : HomeContract.Model {
    override fun listAll() {
        Api(presenter.getContext()).getApiService().getAll().subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({

                    val jobsResponse = JobsResponse()
                    jobsResponse.list = it.subList(0, if (it.size > 30) 30 else it.lastIndex).sortedByDescending { it.id }
                    Log.i("LOG", "List Size: ${jobsResponse.list?.size}")

                    presenter.manipulateResponse(jobsResponse)
                }, { error ->
                    Log.i("LOG", " Error: ${error.message}")
                    presenter.showError(error.message as String)
                })
    }

    override fun search(query: String) {
        Api(presenter.getContext()).getApiService().search(query).subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    val jobsResponse = JobsResponse()
                    jobsResponse.list = it.subList(0, if (it.size > 30) 30 else it.lastIndex).sortedByDescending { it.id }
                    Log.i("LOG", "List Size: ${jobsResponse.list?.size}")

                    presenter.manipulateResponse(jobsResponse)
                }, { error ->
                    Log.i("LOG", "Error: ${error.message}")
                    presenter.showError(error.message as String)
                })
    }

    override fun listFromBD() {

        presenter.getActivity().doAsync {
            AppDatabase.getInstance(presenter.getContext()).jobsDAO().getAll().observeForever {
                presenter.getActivity()?.runOnUiThread {
                    presenter.manipulateResponseDB(it?.subList(0, if (it.size > 30) 30 else it.lastIndex)?.sortedByDescending { it.id })
                }
            }
        }
    }
}
