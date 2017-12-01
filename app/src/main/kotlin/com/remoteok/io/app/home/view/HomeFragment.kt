package com.remoteok.io.app.home.view

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.transition.ChangeBounds
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.remoteok.io.app.R
import com.remoteok.io.app.base.extension.*
import com.remoteok.io.app.detail.view.DetailActivity
import com.remoteok.io.app.home.model.domain.Job
import com.remoteok.io.app.home.viewModel.HomeViewModel
import com.remoteok.io.app.home.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory(context)).get(HomeViewModel::class.java)
    }

    private var list: List<Job>? = ArrayList()
    lateinit var adapter: HomeRecyclerAdapter
    private var adapterSearch: ArrayAdapter<String>? = null
    private var suggestions: Array<String>? = null
    private val filteredValues = ArrayList<String>()

    override fun getContext(): Context = activity as Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        suggestions = resources.getStringArray(R.array.suggestions)
        adapterSearch = ArrayAdapter(context, R.layout.item_search, resources.getStringArray(R.array.suggestions))
        adapter = HomeRecyclerAdapter(activity, list, this::onItemClick)
        setHasOptionsMenu(true)
        retainInstance = true

        observeLoadingStatus()
        observeErrorStatus()
        observeResponse()
        observeResponseFromDataBase()
        getAllJobs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setAnimation()
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener({ getAllJobs() })

        listSearch.setOnItemClickListener { _, _, position, _ ->
            showProgress(true)
            viewModel.search("remote-${filteredValues[position]}-jobs.json")
            listSearch.visibility = GONE
            activity?.hideSoftKeyboard()
        }
    }

    private fun getAllJobs() {
        if (activity?.isConected() == true) {
            viewModel.getAllJobs()
        } else {
            viewModel.getAllJobsDataBase()
        }
    }

    private fun observeLoadingStatus() {
        viewModel.getLoadingStatus().observe(this, android.arch.lifecycle.Observer { isLoading -> showProgress(isLoading) })
    }

    private fun observeErrorStatus() {
        viewModel.getErrorStatus().observe(this, android.arch.lifecycle.Observer { msg -> showSnackBarError(msg.toString()) })
    }

    private fun observeResponse() {
        viewModel.getResponse().observe(this, android.arch.lifecycle.Observer { response -> showJobsList(response) })
    }

    private fun observeResponseFromDataBase() {
        viewModel.getResponseFromDataBase().observe(this, android.arch.lifecycle.Observer { response -> showJobsList(response) })
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val changeBounds = ChangeBounds()
            changeBounds.duration = 2000
            activity?.window?.sharedElementExitTransition = changeBounds
        }
    }

    fun showProgress(b: Boolean?) {
        activity?.showProgress(recyclerView, progress, b == true)
        swipeRefreshLayout?.isRefreshing = false
    }

    fun showSnackBarError(str: String) {
        activity?.showSnackBarError(recyclerView, str)
        swipeRefreshLayout?.isRefreshing = false
    }

    fun showJobsList(jobs: List<Job>?) {

        list = jobs

        withoutData?.visibility = GONE
        if (list?.isEmpty() == true) {
            withoutData?.visibility = VISIBLE
        }

        adapter.update(list)
        swipeRefreshLayout?.isRefreshing = false
        showProgress(false)
    }

    private fun onItemClick(job: Job, imageView: ImageView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context as Activity, Pair.create(imageView, "image"))

        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("job", job)
        context.startActivity(intent, options.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        setUpSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpSearchView(menu: Menu?) {

        val searchView = MenuItemCompat.getActionView(menu?.findItem(R.id.search)) as SearchView
        searchView.setIconifiedByDefault(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search("remote-$query-jobs.json")
                activity?.hideSoftKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isBlank() || newText.length < 3) {
                    listSearch?.visibility = GONE
                    return false
                }

                filteredValues.clear()
                resources.getStringArray(R.array.suggestions).forEach {
                    if (it.toLowerCase().startsWith(newText.toLowerCase())) {
                        filteredValues.add(it)
                    }
                }

                if (filteredValues.size == 0) {
                    listSearch?.visibility = GONE
                }

                adapterSearch = ArrayAdapter(context, R.layout.item_search, filteredValues)
                listSearch?.visibility = VISIBLE
                listSearch?.adapter = adapterSearch

                return true
            }
        })
        MenuItemCompat.setOnActionExpandListener(menu?.findItem(R.id.search),
                object : MenuItemCompat.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                        searchView.requestFocus()
                        activity?.showSoftKeyboard()
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        activity?.hideSoftKeyboard()
                        return true
                    }
                })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            return super.onOptionsItemSelected(item)
        }

        if (item.itemId == R.id.donate) {
            //activity?.browse("https://www.paypal.com/your_paypal")
            activity?.alert {
                title = "Donate"
                message = "Option is disable for now"
                yesButton { }
            }?.show()
        }

        return true
    }
}
