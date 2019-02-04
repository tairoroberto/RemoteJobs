package com.remotejobs.io.app.home.view

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.instantapps.InstantApps
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.hsalf.smilerating.BaseRating
import com.hsalf.smilerating.SmileRating
import com.remotejobs.io.app.R
import com.remotejobs.io.app.data.database.AppDatabase
import com.remotejobs.io.app.data.database.dao.FavoritesDao
import com.remotejobs.io.app.detail.DetailActivity
import com.remotejobs.io.app.home.repository.FavoritesLocalDataStore
import com.remotejobs.io.app.home.repository.HomeLocalDataStore
import com.remotejobs.io.app.home.repository.HomeRemoteDataStore
import com.remotejobs.io.app.home.usecase.HomeUseCase
import com.remotejobs.io.app.home.viewmodel.HomeViewModel
import com.remotejobs.io.app.home.viewmodel.HomeViewModelFactory
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.hideSoftKeyboard
import com.remotejobs.io.app.utils.extension.launchPlayStore
import com.remotejobs.io.app.utils.extension.showSnackBarError
import com.remotejobs.io.app.utils.extension.showSoftKeyboard
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val favoritesDao: FavoritesDao by lazy {
        AppDatabase.getInstance(context).favoritesDaoDao()
    }

    private val viewModel by lazy {
        val local = HomeLocalDataStore(AppDatabase.getInstance(context).jobsDAO())
        val remote = HomeRemoteDataStore()
        val favorites = FavoritesLocalDataStore(AppDatabase.getInstance(context).favoritesDaoDao())
        val useCase = HomeUseCase(local, remote, favorites)
        ViewModelProviders.of(this, HomeViewModelFactory(useCase)).get(HomeViewModel::class.java)
    }

    private val list: MutableList<Job> = ArrayList()

    private lateinit var adapter: JobsRecyclerAdapter

    private lateinit var adapterSearch: SearchAdapter

    private val suggestions: MutableList<String> = ArrayList()

    private val filteredValues = ArrayList<String>()

    private lateinit var tracker: FirebaseAnalytics

    companion object {
        const val SEARCH_PARAM = "getJobs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resources.getStringArray(R.array.suggestions).forEach { suggestions.add(it) }
        adapter = JobsRecyclerAdapter(list, this::onItemClick)
        setHasOptionsMenu(true)
        retainInstance = true

        observeLoadingStatus()
        observeResponse()
        showAlertDialogHateUs()
        tracker = FirebaseAnalytics.getInstance(context as Context)

        if (InstantApps.isInstantApp(context as Context)) {
            tracker.logEvent("home_intantapp", null)
        } else {
            tracker.logEvent("home", null)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterSearch = SearchAdapter(suggestions, favoritesDao)

        setRecyclerViewListJobs()


        listSearch.setOnItemClickListener { _, _, position, _ ->
            viewModel.getJobs(/*"remote-${filteredValues[position]}-jobs"*/)
            listSearch.visibility = GONE
            activity?.hideSoftKeyboard()
        }

        if (arguments?.get(SEARCH_PARAM) != null) {
            viewModel.getJobs(/*"remote-${arguments?.get(SEARCH_PARAM)}-jobs"*/)
        } else {
            viewModel.getAllJobs()
        }
    }

    private fun setRecyclerViewListJobs() {
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            adapter.clear()
            viewModel.getAllJobs()
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val totalItemCount = layoutManager.itemCount
                    val lastVisible = layoutManager.findLastVisibleItemPosition()

                    val endHasBeenReached = lastVisible + 1 >= totalItemCount

                    if (viewModel.loadingStatus.value == false && totalItemCount > 0 && endHasBeenReached) {
                        viewModel.getJobs()
                    }
                }
            }
        })
    }


    private fun observeLoadingStatus() {
        viewModel.loadingStatus.observe(this, Observer { isLoading -> showProgress(isLoading) })
    }

    private fun observeErrorStatus() {
        viewModel.errorStatus.observe(this, Observer { msg -> showSnackBarError(msg.toString()) })
    }

    private fun observeResponse() {
        viewModel.response.observe(this, Observer { response -> showJobsList(response.toMutableList()) })
    }

    private fun showProgress(b: Boolean?) {
        progress.visibility = if (b == true) VISIBLE else GONE
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun showSnackBarError(str: String) {
        activity?.showSnackBarError(recyclerView, str)
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun showJobsList(jobs: MutableList<Job>) {

        withoutData?.visibility = GONE
        if (jobs.isEmpty()) {
            withoutData?.visibility = VISIBLE
        }

        adapter.update(jobs)
    }

    private fun onItemClick(job: Job, imageView: ImageView, textViewTitle: TextView, textViewDate: TextView) {

        val logoTransition: Pair<View, String> = Pair.create(imageView, "logo")
        val titleTransition: Pair<View, String> = Pair.create(textViewTitle, "title")
        val dateTransition: Pair<View, String> = Pair.create(textViewDate, "date")
        val options: ActivityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context as Activity, logoTransition, titleTransition, dateTransition)

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
                viewModel.getJobs(/*"remote-$query-jobs"*/)
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

        val ratedCount: Int = activity?.getSharedPreferences("Home", MODE_PRIVATE)?.getInt("ratedCount", 0) as Int

        activity?.getSharedPreferences("Home", MODE_PRIVATE)?.edit()?.putInt("ratedCount", ratedCount.plus(1))?.apply()

        if (ratedCount > 5 && ratedCount % 5 == 0) {

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
            textInputTellUs.visibility = VISIBLE
            smileRating.setOnSmileySelectionListener { smiley, reselected ->
                when (smiley) {
                    SmileRating.TERRIBLE -> {
                        feeling = "TERRIBLE"
                    }
                    SmileRating.BAD -> {
                        feeling = "BAD"
                    }
                    SmileRating.OKAY -> {
                        feeling = "OKAY"
                    }
                    SmileRating.GOOD -> {
                        feeling = "GOOD"
                    }
                    SmileRating.GREAT -> {
                        feeling = "GREAT"
                    }
                }
            }

            alertBuilder.setView(view)
            alertDialog = alertBuilder.create()
            alertDialog.show()

            activity?.getSharedPreferences("Home", MODE_PRIVATE)?.edit()?.putInt("ratedCount", 0)?.apply()
        }
    }

    private fun showAlert() {
        activity?.alert {

            title = "Rate us on PlayStore"
            message =
                    "Rate us and help us improve with new features for the community, give us 5 stars to help in the disclosure, but leave a comment to improve the app :)"

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
