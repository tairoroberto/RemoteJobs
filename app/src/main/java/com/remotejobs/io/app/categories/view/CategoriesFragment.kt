package com.remotejobs.io.app.categories.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.R
import com.remotejobs.io.app.categories.repository.CategoriesRemoteDataStore
import com.remotejobs.io.app.categories.usecase.CategoriesUseCase
import com.remotejobs.io.app.categories.viewmodel.CategoriesViewModel
import com.remotejobs.io.app.categories.viewmodel.CategoriesViewModelFactory
import com.remotejobs.io.app.utils.extension.showProgress
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment() {

    private lateinit var adapter: CategoriesRecyclerAdapter

    private val list: MutableList<String> = ArrayList()

    private val viewModel by lazy {
        val remote = CategoriesRemoteDataStore()
        val useCase = CategoriesUseCase(remote)
        ViewModelProviders.of(this, CategoriesViewModelFactory(useCase)).get(CategoriesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCategories()
        viewModel.response.observe(this, Observer {
            adapter.update(it)
        })
        viewModel.loadingStatus.observe(this, Observer { isLoading -> showProgress(isLoading) })

        setHasOptionsMenu(true)
        retainInstance = true
        FirebaseAnalytics.getInstance(context as Context).logEvent("highestpaid", null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoriesRecyclerAdapter(activity, list, this::onItemClick)
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun onItemClick(tag: String) {
        val intent = Intent(activity, CategoriesSelectActivity::class.java)
        intent.putExtra("tag", tag)
        startActivity(intent)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    private fun showProgress(b: Boolean?) {
        activity?.showProgress(recyclerView, progress, b == true)
    }
}
