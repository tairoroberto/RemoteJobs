package br.com.tairoroberto.remoteok.home.model

import android.util.Log
import br.com.tairoroberto.remoteok.R
import br.com.tairoroberto.remoteok.home.contract.HomeContract
import br.com.tairoroberto.remoteok.home.model.AppDatabase
import br.com.tairoroberto.remoteok.home.model.domain.JobsResponse
import com.tairoroberto.nextel.base.api.ApiUtils

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by tairo on 12/11/17.
 */
class HomeModel(private val presenter: HomeContract.Presenter) : HomeContract.Model {
    override fun listAll() {
        ApiUtils.getApiService()?.getAll()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({

                    val jobs = JobsResponse()
                    jobs.list = it
                    jobs.list = jobs.list?.subList(0, 50)?.sortedBy { it.date }
                    Log.i("LOG", "List Size: ${jobs.list?.size}")

                    presenter.manipulateResponse(jobs)
                }, { error ->
                    Log.i("LOG", " Error: ${error.message}")
                    presenter.showError(error.message as String)
                })
    }

    override fun search(query: String) {
        ApiUtils.getApiService()?.search(query)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    val jobsResponse = JobsResponse()
                    jobsResponse.list = it
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
                    presenter.manipulateResponseDB(it)
                }
            }
        }
    }
}
