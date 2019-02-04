package com.remotejobs.io.app.companies.view

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.R
import com.remotejobs.io.app.companies.repository.CompaniesLocalDataStore
import com.remotejobs.io.app.companies.repository.CompaniesRemoteDataStore
import com.remotejobs.io.app.companies.usecase.CompaniesUseCase
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModel
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModelFactory
import com.remotejobs.io.app.data.database.AppDatabase
import com.remotejobs.io.app.detail.DetailActivity
import com.remotejobs.io.app.home.view.JobsRecyclerAdapter
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.showProgress
import kotlinx.android.synthetic.main.fragment_companies_jobs.*

class CompaniesSelectActivity : AppCompatActivity() {

    private lateinit var adapter: JobsRecyclerAdapter

    private val list: MutableList<Job> = ArrayList()
    private var company: String? = ""

    private val viewModel by lazy {
        val local = CompaniesLocalDataStore(
                AppDatabase.getInstance(this).companiesDAO(),
                AppDatabase.getInstance(this).jobsDAO()
        )
        val remote = CompaniesRemoteDataStore()
        val useCase = CompaniesUseCase(local, remote)
        ViewModelProviders.of(this, CompaniesViewModelFactory(useCase)).get(CompaniesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_companies_jobs)

        company = intent?.getStringExtra("company")

        if (company != null) {
            viewModel.listCompaniesJobs(company as String)
        } else {
            finish()
        }

        setListAdapter()
        viewModel.responseJobs.observe(this, Observer {
            adapter.update(it.toMutableList())
            swipeRefreshLayout?.isRefreshing = false
        })

        viewModel.errorStatus.observe(this, Observer {
            withoutData.visibility = VISIBLE
        })

        viewModel.loadingStatus.observe(
                this,
                Observer { isLoading -> showProgress(recyclerView, progress, isLoading == true) })
        FirebaseAnalytics.getInstance(this).logEvent("company_jobs", null)
    }

    private fun setListAdapter() {
        adapter = JobsRecyclerAdapter(list, this::onItemClick)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            adapter.clear()
            viewModel.lastItem = null
            viewModel.listCompaniesJobs(company as String)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val totalItemCount = layoutManager.itemCount
                    val lastVisible = layoutManager.findLastVisibleItemPosition()

                    val endHasBeenReached = lastVisible + 1 >= totalItemCount

                    if (viewModel.loadingStatus.value == false && totalItemCount > 0 && endHasBeenReached) {
                        viewModel.listCompaniesJobs(company as String)
                    }
                }
            }
        })
    }

    private fun onItemClick(job: Job, imageView: ImageView, textViewTitle: TextView, textViewDate: TextView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, Pair.create(imageView, "image"))

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent, options.toBundle())
    }
}
