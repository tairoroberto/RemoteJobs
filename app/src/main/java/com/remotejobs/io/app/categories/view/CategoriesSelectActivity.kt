package com.remotejobs.io.app.categories.view

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
import com.remotejobs.io.app.categories.repository.CategoriesRemoteDataStore
import com.remotejobs.io.app.categories.usecase.CategoriesUseCase
import com.remotejobs.io.app.categories.viewmodel.CategoriesViewModel
import com.remotejobs.io.app.categories.viewmodel.CategoriesViewModelFactory
import com.remotejobs.io.app.detail.DetailActivity
import com.remotejobs.io.app.home.view.JobsRecyclerAdapter
import com.remotejobs.io.app.model.Job
import kotlinx.android.synthetic.main.fragment_jobs_category.*


class CategoriesSelectActivity : AppCompatActivity() {

    private lateinit var adapter: JobsRecyclerAdapter

    private val list: MutableList<Job> = ArrayList()
    private var tag: String? = ""

    private val viewModel by lazy {
        val remote = CategoriesRemoteDataStore()
        val useCase = CategoriesUseCase(remote)
        ViewModelProviders.of(this, CategoriesViewModelFactory(useCase))
            .get(CategoriesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_jobs_category)

        tag = intent?.getStringExtra("tag")

        if (tag != null) {
            viewModel.getJobsByCategory(tag as String)
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
            Observer { isLoading ->
                progress.visibility = if (isLoading) VISIBLE else GONE
            })

        FirebaseAnalytics.getInstance(this).logEvent("highestpaid_jobs", null)
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
            viewModel.getJobsByCategory(tag as String)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val totalItemCount = layoutManager.itemCount
                    val lastVisible = layoutManager.findLastVisibleItemPosition()

                    val endHasBeenReached = lastVisible + 1 >= totalItemCount

                    if (viewModel.loadingStatus.value == false && totalItemCount > 0 && endHasBeenReached) {
                        viewModel.getJobsByCategory(tag as String)
                    }
                }
            }
        })
    }

    private fun onItemClick(
        job: Job,
        content: ViewGroup,
        imageView: ImageView,
        textViewTitle: TextView,
        textViewDate: TextView
    ) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
            .makeSceneTransitionAnimation(this, Pair.create(imageView, "image"))

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent, options.toBundle())
    }
}
