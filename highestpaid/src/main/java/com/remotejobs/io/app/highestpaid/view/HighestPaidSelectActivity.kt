package com.remotejobs.io.app.highestpaid.view

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View.VISIBLE
import android.widget.ImageView
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.highestpaid.R
import com.remotejobs.io.app.highestpaid.di.HighestPaidModuleInjector
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModel
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModelFactory
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.showProgress
import com.remotejobs.io.app.view.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_highestpaid_jobs.*
import javax.inject.Inject

class HighestPaidSelectActivity : AppCompatActivity() {

    private lateinit var adapter: HighestpaidJobsRecyclerAdapter

    private val list: MutableList<Job> = ArrayList()
    private var tag: String? = ""

    @Inject
    lateinit var highestPaidViewModelFactory: HighestPaidViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, highestPaidViewModelFactory).get(HighestPaidViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HighestPaidModuleInjector.inject(this)

        setContentView(R.layout.fragment_highestpaid_jobs)

        tag = intent?.getStringExtra("tag")

        if (tag != null) {
            viewModel.loadHighestPaidJobs(tag as String)
        } else {
            finish()
        }

        setListAdapter()
        viewModel.getHighestPaidJobsResponse().observe(this, Observer {

            if (it?.isEmpty() == true) {
                withoutData.visibility = VISIBLE
            } else {
                adapter.update(it)
            }
            swipeRefreshLayout?.isRefreshing = false
        })

        viewModel.getLoadingStatus().observe(this, androidx.lifecycle.Observer { isLoading -> showProgress(recyclerView, progress, isLoading == true) })
        FirebaseAnalytics.getInstance(this).logEvent("highestpaid_jobs", null)
    }

    private fun setListAdapter() {
        adapter = HighestpaidJobsRecyclerAdapter(this, list, this::onItemClick)
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener({ viewModel.loadHighestPaidJobs(tag as String) })
    }

    private fun onItemClick(job: Job, imageView: ImageView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, Pair.create(imageView, "image"))

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent, options.toBundle())
    }
}
