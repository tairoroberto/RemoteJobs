package com.remoteok.io.app.view.companies


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.analytics.FirebaseAnalytics
import com.remoteok.io.app.R
import com.remoteok.io.app.model.Company
import com.remoteok.io.app.utils.extension.showProgress
import com.remoteok.io.app.viewmodel.companies.CompaniesViewModel
import com.remoteok.io.app.viewmodel.companies.CompaniesViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_companies.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class CompaniesFragment : Fragment() {

    private lateinit var adapter: CompaniesRecyclerAdapter

    private val list: MutableList<Company> = ArrayList()

    @Inject
    lateinit var companiesViewModelFactory: CompaniesViewModelFactory

    private val viewModel by lazy {
        ViewModelProviders.of(this, companiesViewModelFactory).get(CompaniesViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.listAllCompanies()
        viewModel.getResponse().observe(this, Observer {
            adapter.update(it)
        })
        viewModel.getLoadingStatus().observe(this, android.arch.lifecycle.Observer { isLoading -> showProgress(isLoading) })

        setHasOptionsMenu(true)
        retainInstance = true
        FirebaseAnalytics.getInstance(activity).logEvent("companies", null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CompaniesRecyclerAdapter(activity, list, this::onItemClick)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun onItemClick(company: Company, imageView: ImageView) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_companies, container, false)
    }

    private fun showProgress(b: Boolean?) {
        activity?.showProgress(recyclerView, progress, b == true)
    }
}
