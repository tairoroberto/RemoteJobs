package com.remotejobs.io.app.companies.view


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
import com.remotejobs.io.app.R
import com.remotejobs.io.app.companies.repository.CompaniesLocalDataStore
import com.remotejobs.io.app.companies.repository.CompaniesRemoteDataStore
import com.remotejobs.io.app.companies.usecase.CompaniesUseCase
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModel
import com.remotejobs.io.app.companies.viewmodel.CompaniesViewModelFactory
import com.remotejobs.io.app.data.database.AppDatabase
import com.remotejobs.io.app.model.Company
import com.remotejobs.io.app.utils.extension.showProgress
import kotlinx.android.synthetic.main.fragment_companies.*

/**
 * A simple [Fragment] subclass.
 */
class CompaniesFragment : Fragment() {

    private lateinit var adapter: CompaniesRecyclerAdapter

    private val list: MutableList<Company> = ArrayList()

    private val viewModel by lazy {
        val local = CompaniesLocalDataStore(
            AppDatabase.getInstance(context).companiesDAO(),
            AppDatabase.getInstance(context).jobsDAO()
        )
        val remote = CompaniesRemoteDataStore()
        val useCase = CompaniesUseCase(local, remote)
        ViewModelProviders.of(this, CompaniesViewModelFactory(useCase)).get(CompaniesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.listAllCompanies()
        viewModel.response.observe(this, Observer {
            adapter.update(it)
        })
        viewModel.loadingStatus.observe(this, Observer { isLoading -> showProgress(isLoading) })

        setHasOptionsMenu(true)
        retainInstance = true
        FirebaseAnalytics.getInstance(context as Context).logEvent("companies", null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CompaniesRecyclerAdapter(activity, list, this::onItemClick)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun onItemClick(company: Company) {
        val intent = Intent(activity, CompaniesSelectActivity::class.java)
        intent.putExtra("company", company.company)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_companies, container, false)
    }

    private fun showProgress(b: Boolean?) {
        activity?.showProgress(recyclerView, progress, b == true)
    }
}
