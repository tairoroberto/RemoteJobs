package com.remoteok.io.app.view.companies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View.VISIBLE
import android.widget.ImageView
import com.google.firebase.analytics.FirebaseAnalytics
import com.remoteok.io.app.R
import com.remoteok.io.app.model.Job
import com.remoteok.io.app.utils.extension.showProgress
import com.remoteok.io.app.view.detail.DetailActivity
import com.remoteok.io.app.view.home.HomeRecyclerAdapter
import com.remoteok.io.app.viewmodel.companies.CompaniesViewModel
import com.remoteok.io.app.viewmodel.companies.CompaniesViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class CompaniesSelectActivity : AppCompatActivity() {

    private lateinit var adapter: HomeRecyclerAdapter

    private val list: MutableList<Job> = ArrayList()
    private var company: String? = ""

    @Inject
    lateinit var companiesViewModelFactory: CompaniesViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, companiesViewModelFactory).get(CompaniesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        setContentView(R.layout.fragment_home)

        company = intent?.getStringExtra("company")

        if (company != null) {
            viewModel.listCompaniesJobs(company as String)
        } else {
            finish()
        }

        setListAdapter()
        viewModel.getCompanyJobsResponse().observe(this, Observer {

            if (it?.isEmpty() == true) {
                withoutData.visibility = VISIBLE
            } else {
                adapter.update(it)
            }
            swipeRefreshLayout?.isRefreshing = false
        })

        viewModel.getLoadingStatus().observe(this, android.arch.lifecycle.Observer { isLoading -> showProgress(recyclerView, progress, isLoading == true) })
        FirebaseAnalytics.getInstance(this).logEvent("company_jobs", null)
    }

    private fun setListAdapter() {
        adapter = HomeRecyclerAdapter(this, list, this::onItemClick)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener({ viewModel.listCompaniesJobs(company as String) })
    }

    private fun onItemClick(job: Job, imageView: ImageView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, Pair.create(imageView, "image"))

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent, options.toBundle())
    }
}
