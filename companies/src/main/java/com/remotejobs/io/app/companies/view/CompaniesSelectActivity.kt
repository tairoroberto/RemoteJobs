package com.remotejobs.io.app.companies.view

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
import com.remotejobs.io.app.companies.R
import com.remotejobs.io.app.companies.di.CompaniesModuleInjector
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModel
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModelFactory
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.showProgress
import com.remotejobs.io.app.view.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_companies_jobs.*
import javax.inject.Inject

class CompaniesSelectActivity : AppCompatActivity() {

    private lateinit var adapter: CompaniesJobsRecyclerAdapter

    private val list: MutableList<Job> = ArrayList()
    private var company: String? = ""

    @Inject
    lateinit var companiesViewModelFactory: CompaniesViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, companiesViewModelFactory).get(CompaniesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CompaniesModuleInjector.inject(this)

        setContentView(R.layout.fragment_companies_jobs)

        company = intent?.getStringExtra("company")

        if (company != null) {
            viewModel.listCompaniesJobs(company as String)
        } else {
            finish()
        }

        setListAdapter()
        viewModel.responseJobs.observe(this, Observer {

            if (it?.isEmpty() == true) {
                withoutData.visibility = VISIBLE
            } else {
                adapter.update(it)
            }
            swipeRefreshLayout?.isRefreshing = false
        })

        viewModel.loadingStatus.observe(this, Observer { isLoading -> showProgress(recyclerView, progress, isLoading == true) })
        FirebaseAnalytics.getInstance(this).logEvent("company_jobs", null)
    }

    private fun setListAdapter() {
        adapter = CompaniesJobsRecyclerAdapter(this, list, this::onItemClick)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener { viewModel.listCompaniesJobs(company as String) }
    }

    private fun onItemClick(job: Job, imageView: ImageView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, Pair.create(imageView, "image"))

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent, options.toBundle())
    }
}
