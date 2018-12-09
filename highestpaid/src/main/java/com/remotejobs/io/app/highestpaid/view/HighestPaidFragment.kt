package com.remotejobs.io.app.highestpaid.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.highestpaid.R
import com.remotejobs.io.app.highestpaid.di.HighestPaidModuleInjector
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModel
import com.remotejobs.io.app.highestpaid.viewmodel.HighestPaidViewModelFactory
import com.remotejobs.io.app.model.HighestPaid
import com.remotejobs.io.app.utils.extension.showProgress
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
        HighestPaidModuleInjector.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllHighestPaidSalaries()
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

        adapter = HighestPaidRecyclerAdapter(activity, list, this::onItemClick)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun onItemClick(tag: String) {
        val intent = Intent(activity, HighestPaidSelectActivity::class.java)
        intent.putExtra("tag", tag)
        startActivity(intent)
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
