package com.remotejobs.io.app.highestpaid.view

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.R
import com.remotejobs.io.app.data.database.AppDatabase
import com.remotejobs.io.app.detail.DetailActivity
import com.remotejobs.io.app.highestpaid.repository.HighestPaidLocalDataStore
import com.remotejobs.io.app.highestpaid.repository.HighestPaidRemoteDataStore
import com.remotejobs.io.app.highestpaid.usecase.HighestPaidUseCase
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModel
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModelFactory
import com.remotejobs.io.app.home.view.JobsRecyclerAdapter
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.showProgress
import kotlinx.android.synthetic.main.fragment_highestpaid_jobs.*

class HighestPaidSelectActivity : AppCompatActivity() {

    private lateinit var adapter: JobsRecyclerAdapter

    private val list: MutableList<Job> = ArrayList()
    private var tag: String? = ""

    private val viewModel by lazy {
        val local = HighestPaidLocalDataStore(
            AppDatabase.getInstance(this).highestPaidDao(),
            AppDatabase.getInstance(this).jobsDAO()
        )
        val remote = HighestPaidRemoteDataStore()
        val useCase = HighestPaidUseCase(local, remote)
        ViewModelProviders.of(this, HighestPaidViewModelFactory(useCase)).get(HighestPaidViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_highestpaid_jobs)

        tag = intent?.getStringExtra("tag")

        if (tag != null) {
            viewModel.loadHighestPaidJobs(tag as String)
        } else {
            finish()
        }

        setListAdapter()
        viewModel.responseJobs.observe(this, Observer {

            if (it?.isEmpty() == true) {
                withoutData.visibility = VISIBLE
            } else {
                adapter.update(it.toMutableList())
            }
            swipeRefreshLayout?.isRefreshing = false
        })

        viewModel.loadingStatus.observe(
            this,
            Observer { isLoading -> showProgress(recyclerView, progress, isLoading == true) })
        FirebaseAnalytics.getInstance(this).logEvent("highestpaid_jobs", null)
    }

    private fun setListAdapter() {
        adapter = JobsRecyclerAdapter(list, this::onItemClick)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener { viewModel.loadHighestPaidJobs(tag as String) }
    }

    private fun onItemClick(job: Job, imageView: ImageView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
            .makeSceneTransitionAnimation(this, Pair.create(imageView, "image"))

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent, options.toBundle())
    }
}
