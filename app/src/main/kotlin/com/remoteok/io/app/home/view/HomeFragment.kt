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
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.transition.ChangeBounds
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.remoteok.io.app.R
import com.remoteok.io.app.base.extension.isConected
import com.remoteok.io.app.base.extension.showProgress
import com.remoteok.io.app.base.extension.showSnackBarError
import com.remoteok.io.app.detail.view.DetailActivity
import com.remoteok.io.app.home.contract.HomeContract
import com.remoteok.io.app.home.model.domain.Job
import com.remoteok.io.app.home.presenter.HomePresenter
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), HomeContract.View, OnClick {

    private val presenter: HomeContract.Presenter by lazy {
        ViewModelProviders.of(this).get(HomePresenter::class.java)
    }

    private var list: ArrayList<Job>? = ArrayList()
    private var adapter: HomeRecyclerAdapter? = null
    private var adapterSearch: ArrayAdapter<String>? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progress: ProgressBar? = null
    private var withoutData: TextView? = null
    private var listSearch: ListView? = null
    private var suggestions: Array<String>? = null
    private val filteredValues = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        suggestions = resources.getStringArray(R.array.suggestions)
        adapterSearch = ArrayAdapter(context, R.layout.item_search, resources.getStringArray(R.array.suggestions))

        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setAnimation()
        val view: View? = inflater.inflate(R.layout.fragment_list, container, false)

        val layoutManager = LinearLayoutManager(activity)
        recyclerView = view?.findViewById(R.id.recyclerView)
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        progress = view?.findViewById(R.id.progress)
        withoutData = view?.findViewById(R.id.withoutData)
        listSearch = view?.findViewById(R.id.listSearch)

        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = HomeRecyclerAdapter(activity, list, this::onItemClick)
        recyclerView?.adapter = adapter
        swipeRefreshLayout?.setOnRefreshListener({ loadJobs() })
        listSearch?.setOnItemClickListener { _, _, position, _ ->
            showProgress(true)
            presenter.search("remote-${filteredValues[position]}-jobs.json")
            listSearch?.visibility = GONE
            hideSoftKeyboard()
        }

        showProgress(true)
        loadJobs()

        return view
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val changeBounds = ChangeBounds()
            changeBounds.duration = 2000
            activity?.window?.sharedElementExitTransition = changeBounds
        }
    }

    private fun loadJobs() = if (activity?.isConected() == true) {
        presenter.loadJobs()
    } else {
        presenter.loadFromBD()
    }

    override fun showProgress(b: Boolean) {
        activity?.showProgress(recyclerView, progress, b)
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showSnackBarError(str: String) {
        activity?.showSnackBarError(recyclerView, str)
    }

    override fun showJobsList(jobs: List<Job>?) {
        val list: ArrayList<Job> = ArrayList()

        jobs?.forEach { list.add(it) }

        withoutData?.visibility = GONE
        if (list.isEmpty()) {
            withoutData?.visibility = VISIBLE
        }

        adapter?.update(list)
        swipeRefreshLayout?.isRefreshing = false
        showProgress(false)
    }

    override fun onItemClick(job: Job, imageView: ImageView) {

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
                presenter.search("remote-$query-jobs.json")
                hideSoftKeyboard()
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

                if (filteredValues.size == 0){
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
                        showSoftKeyboard()
                        return true
                    }

                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        hideSoftKeyboard()
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

    private fun hideSoftKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as? InputMethodManager)?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showSoftKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(view, 0)
        }
    }

    override fun getContext(): Context = activity as Context
}