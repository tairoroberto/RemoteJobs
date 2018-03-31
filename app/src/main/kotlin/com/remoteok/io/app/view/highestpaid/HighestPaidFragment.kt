package com.remoteok.io.app.view.highestpaid

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.remoteok.io.app.R
import com.remoteok.io.app.model.HighestPaid
import com.remoteok.io.app.utils.extension.showProgress
import com.remoteok.io.app.viewmodel.highestpaid.HighestPaidViewModel
import com.remoteok.io.app.viewmodel.highestpaid.HighestPaidViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_highest_paid.*
import javax.inject.Inject

class HighestPaidFragment : Fragment() {

    private lateinit var adapter: HighestPaidRecyclerAdapter

    private val list: MutableList<HighestPaid> = ArrayList()

    @Inject
    lateinit var highestPaidViewModelFactory: HighestPaidViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, highestPaidViewModelFactory).get(HighestPaidViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllHighestPaidSalaries()
        viewModel.getResponse().observe(this, Observer {
            adapter.update(it)
        })
        viewModel.getLoadingStatus().observe(this, android.arch.lifecycle.Observer { isLoading -> showProgress(isLoading) })

        setHasOptionsMenu(true)
        retainInstance = true
        FirebaseAnalytics.getInstance(activity).logEvent("highestpaid", null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HighestPaidRecyclerAdapter(activity, list, this::onItemClick)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun onItemClick(tag: String) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_highest_paid, container, false)
    }

    private fun showProgress(b: Boolean?) {
        activity?.showProgress(recyclerView, progress, b == true)
    }
}
