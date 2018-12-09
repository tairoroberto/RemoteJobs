package com.remotejobs.io.app.home.view

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.instantapps.InstantApps
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsalf.smilerating.BaseRating
import com.hsalf.smilerating.SmileRating
import com.remotejobs.io.app.data.dao.FavoritesDao
import com.remotejobs.io.app.home.R
import com.remotejobs.io.app.home.di.HomeModuleInjector
import com.remotejobs.io.app.home.viewmodel.HomeViewModel
import com.remotejobs.io.app.home.viewmodel.HomeViewModelFactory
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.*
import com.remotejobs.io.app.view.detail.DetailActivity
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    @Inject
    lateinit var favoritesDao: FavoritesDao

    private val viewModel by lazy {
        ViewModelProviders.of(this, homeViewModelFactory).get(HomeViewModel::class.java)
    }

    private val list: MutableList<Job> = ArrayList()

    private lateinit var adapter: HomeRecyclerAdapter

    private lateinit var adapterSearch: SearchAdapter

    private val suggestions: MutableList<String> = ArrayList()

    private val filteredValues = ArrayList<String>()

    private lateinit var tracker: FirebaseAnalytics

    companion object {
        const val SEARCH_PARAM = "search"

    }

    override fun onAttach(context: Context?) {
        HomeModuleInjector.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resources.getStringArray(R.array.suggestions).forEach { suggestions.add(it) }
        adapterSearch = SearchAdapter(activity, suggestions, favoritesDao)
        adapter = HomeRecyclerAdapter(list, this::onItemClick)
        setHasOptionsMenu(true)
        retainInstance = true

        observeLoadingStatus()
        //observeErrorStatus()
        observeResponse()
        getAllJobs()
        tracker = FirebaseAnalytics.getInstance(context as Context)

        if (InstantApps.isInstantApp(context as Context)) {
            tracker.logEvent("home_intantapp", null)
        } else {
            tracker.logEvent("home", null)
        }

        if (arguments?.get(SEARCH_PARAM) != null) {
            showProgress(true)
            viewModel.search("remote-${arguments?.get(SEARCH_PARAM)}-jobs")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setAnimation()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewListJobs()

        listSearch.setOnItemClickListener { _, _, position, _ ->
            showProgress(true)
            viewModel.search("remote-${filteredValues[position]}-jobs")
            listSearch.visibility = GONE
            activity?.hideSoftKeyboard()
        }

        if (!InstantApps.isInstantApp(context as Context)) {
            showAlertDialogHateUs()
        }
    }

    private fun setRecyclerViewListJobs() {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener { getAllJobs() }
    }

    private fun getAllJobs() {
        viewModel.getAllJobs()
    }

    private fun observeLoadingStatus() {
        viewModel.loadingStatus.observe(this, Observer { isLoading -> showProgress(isLoading) })
    }

    private fun observeErrorStatus() {
        viewModel.errorStatus.observe(this, Observer { msg -> showSnackBarError(msg.toString()) })
    }

    private fun observeResponse() {
        viewModel.response.observe(this, Observer { response -> showJobsList(response) })
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val changeBounds = ChangeBounds()
            changeBounds.duration = 2000
            activity?.window?.sharedElementExitTransition = changeBounds
        }
    }

    private fun showProgress(b: Boolean?) {
        activity?.showProgress(recyclerView, progress, b == true)
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun showSnackBarError(str: String) {
        activity?.showSnackBarError(recyclerView, str)
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun showJobsList(jobs: List<Job>?) {

        withoutData?.visibility = GONE
        if (jobs == null || jobs.isEmpty()) {
            withoutData?.visibility = VISIBLE
        }

        adapter.update(jobs)
        swipeRefreshLayout?.isRefreshing = false
        showProgress(false)
    }

    private fun onItemClick(job: Job, imageView: ImageView) {

        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context as Activity, Pair.create(imageView, "image"))

        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("job", job)
        activity?.startActivity(intent, options.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        setUpSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpSearchView(menu: Menu?) {

        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setIconifiedByDefault(false)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.search("remote-$query-jobs")
                activity?.hideSoftKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isBlank() || newText.length < 2) {
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

                adapterSearch.updateItems(filteredValues)
                listSearch?.visibility = VISIBLE
                listSearch?.adapter = adapterSearch

                return true
            }
        })

        menu.findItem(R.id.search)?.setOnActionExpandListener(
                object : MenuItem.OnActionExpandListener {
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

        if (item.itemId == R.id.favorites) {
            startActivity(Intent(context, FavoritesActivity::class.java))
        }

        return true
    }

    private fun showAlertDialogHateUs() {

        val rated = activity?.getSharedPreferences("Home", MODE_PRIVATE)?.getBoolean("rated", false)
        val ratedCount: Int = activity?.getSharedPreferences("Home", MODE_PRIVATE)?.getInt("ratedCount", 0) as Int

        if (rated != true || ratedCount % 10 == 0) {

            val alertBuilder = AlertDialog.Builder(context as Context)
            var alertDialog: AlertDialog? = null
            var feeling: String? = "GREAT"
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_rate_us, null)
            val btnOk = view.findViewById<Button>(R.id.btn_ok)
            val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
            val smileRating = view.findViewById<SmileRating>(R.id.smile_rating)
            val textInputTellUs = view.findViewById<TextInputLayout>(R.id.text_input_tell_us)

            btnOk.setOnClickListener {
                alertDialog?.dismiss()
                val value = textInputTellUs.editText?.text.toString()
                trackRateUs(feeling, value)

                if (feeling == "GOOD" || feeling == "GREAT") {
                    showAlert()
                }
            }

            btnCancel.setOnClickListener {
                alertDialog?.dismiss()
            }

            smileRating.selectedSmile = BaseRating.GREAT
            smileRating.setOnSmileySelectionListener { smiley, reselected ->
                when (smiley) {
                    SmileRating.TERRIBLE -> {
                        textInputTellUs.visibility = VISIBLE
                        feeling = "TERRIBLE"
                    }
                    SmileRating.BAD -> {
                        textInputTellUs.visibility = VISIBLE
                        feeling = "BAD"
                    }
                    SmileRating.OKAY -> {
                        textInputTellUs.visibility = VISIBLE
                        feeling = "OKAY"
                    }
                    SmileRating.GOOD -> {
                        textInputTellUs.visibility = GONE
                        feeling = "GOOD"
                    }
                    SmileRating.GREAT -> {
                        textInputTellUs.visibility = GONE
                        feeling = "GREAT"
                    }
                }
            }

            alertBuilder.setView(view)
            alertDialog = alertBuilder.create()
            alertDialog.show()
        }

        activity?.getSharedPreferences("Home", MODE_PRIVATE)?.edit()?.putInt("ratedCount", ratedCount.plus(1))?.apply()
    }

    private fun showAlert() {
        activity?.alert {

            title = "Rate us on PlayStore"
            message = "Rate us and help us improve with new features for the community, give us 5 stars to help in the disclosure, but leave a comment to improve the app :)"

            noButton {}
            yesButton {
                activity?.launchPlayStore()
                activity?.getSharedPreferences("Home", MODE_PRIVATE)?.edit()?.putBoolean("rated", true)?.apply()
            }

        }?.show()
    }

    private fun trackRateUs(feeling: String?, value: String) {
        val bundle = Bundle()
        bundle.putString(feeling, value)
        tracker.logEvent("rate_us", bundle)
        activity?.getSharedPreferences("Home", MODE_PRIVATE)?.edit()?.putBoolean("rated", true)?.apply()
    }
}
