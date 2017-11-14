package br.com.tairoroberto.remoteok.home.view

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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import br.com.tairoroberto.remoteok.home.model.domain.Job
import br.com.tairoroberto.remoteok.R
import br.com.tairoroberto.remoteok.base.extension.isConected
import br.com.tairoroberto.remoteok.base.extension.showProgress
import br.com.tairoroberto.remoteok.base.extension.showSnackBarError
import br.com.tairoroberto.remoteok.home.contract.HomeContract
import br.com.tairoroberto.remoteok.home.presenter.HomePresenter
import br.com.tairoroberto.remoteok.detail.view.DetailActivity

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), HomeContract.View, OnClick {

    private var presenter: HomeContract.Presenter? = null
    private var listMovies: ArrayList<Job>? = ArrayList()
    private var adapter: HomeRecyclerAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var progress: ProgressBar? = null
    private var withoutData: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ViewModelProviders.of(this).get(HomePresenter::class.java)
        presenter?.attachView(this)
        setHasOptionsMenu(true)
    }

    override fun onDestroy() {
        presenter?.detachView()
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

        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        adapter = HomeRecyclerAdapter(activity, listMovies, this)
        recyclerView?.adapter = adapter
        swipeRefreshLayout?.setOnRefreshListener({ loadMovies() })

        showProgress(true)
        loadMovies()

        return view
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val changeBounds = ChangeBounds()
            changeBounds.duration = 2000
            activity?.window?.sharedElementExitTransition = changeBounds
        }
    }

    private fun loadMovies() {
        if (activity?.isConected() == true) {
            presenter?.loadMovies()
        } else {
            presenter?.loadFromBD()
        }
    }

    override fun showProgress(b: Boolean) {
        activity?.showProgress(recyclerView, progress, b)
    }

    override fun showSnackBarError(str: String) {
        activity?.showSnackBarError(recyclerView, str)
    }

    override fun showMoviesList(movies: List<Job>?) {
        val listMovie: ArrayList<Job> = ArrayList()

        movies?.forEach { listMovie.add(it) }

        withoutData?.visibility = GONE
        if (listMovie.isEmpty()) {
            withoutData?.visibility = VISIBLE
        }

        adapter?.update(listMovie)
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
                presenter?.search(query)
                hideSoftKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
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

    override fun getContext(): Context {
        return activity as Context
    }
}
