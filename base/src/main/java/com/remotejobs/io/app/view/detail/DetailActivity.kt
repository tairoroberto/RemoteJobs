package com.remotejobs.io.app.view.detail

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.text.Html
import android.transition.ChangeBounds
import android.view.Menu
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.instantapps.InstantApps
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.BuildConfig
import com.remotejobs.io.app.R
import com.remotejobs.io.app.model.Job
import com.remotejobs.io.app.utils.extension.loadImage
import com.remotejobs.io.app.utils.extension.showProgress
import com.remotejobs.io.app.viewmodel.detail.DetailViewModel
import com.remotejobs.io.app.viewmodel.detail.DetailViewModelFactory
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.jetbrains.anko.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DetailActivity : AppCompatActivity() {

    private var shareActionProvider: ShareActionProvider? = null

    private var job: Job? = Job()

    private lateinit var contentUri: Uri

    private lateinit var tracker: FirebaseAnalytics

    private val viewModel by lazy {
        ViewModelProviders.of(this, DetailViewModelFactory()).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        tracker = FirebaseAnalytics.getInstance(this@DetailActivity)

        showProgress(textViewName, progressBar, true)
        job = intent?.getParcelableExtra("job")

        showJob()

        fab.setOnClickListener { showAlertDialog() }
        imageBack.setOnClickListener {
            setAnimation()
            onBackPressed()
        }

        contentUri = Uri.EMPTY
        val params = Bundle()
        params.putString("job_name", job?.position)
        tracker.logEvent("detail", params)
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val changeBounds = ChangeBounds()
            changeBounds.duration = 2000
            window.sharedElementExitTransition = changeBounds
        }
    }

    private fun showAlertDialog() {
        alert {
            title = "Apply for this job"
            message = "Position: ${job?.position}\n" +
                    "Company: ${job?.company}\n" +
                    "Created at: ${job?.date}"

            noButton {}
            yesButton {
                applyJob()
            }

        }.show()
    }

    private fun applyJob() {

        when {
            job?.urlApply?.equals("/l/${job?.id}") == true -> {
                browse("https://remoteok.io${job?.urlApply}")
                trackApplyedJob()
            }

            job?.urlApply?.contains("mailto:") == true -> {
                startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(job?.urlApply)))
                trackApplyedJob()
            }

            job?.urlApply?.contains("javascript:") == true ->
                alert {
                    title = "Alert"
                    message = "'This job post is older than 90 days and the position is probably filled. Try applying to jobs posted recently instead."
                    okButton { }
                }.show()

            else -> {
                browse("https://remoteok.io/l/${job?.id}")
                trackApplyedJob()
            }
        }
    }

    private fun trackApplyedJob() {
        val params = Bundle()
        params.putString("job_id", job?.id)
        params.putString("job_name", job?.position)
        tracker.logEvent("apply_job", params)
    }

    private fun showJob() {
        imageViewLogo.loadImage(job?.logo, progressImage)

        toolbar_layout.title = job?.position
        textViewName.text = job?.position

        val index = job?.description?.indexOf("<p style=\"text-align:center\">See more jobs") as Int

        val data = if (index >= 0) {
            job?.description?.removeRange(index, job?.description?.length as Int) + "</div>"
        } else {
            job?.description
        }

        textViewReleaseDate.text = job?.date
        webiewViewDescription.loadData(data, "text/html", "UTF-8")
        webiewViewDescription.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                showProgress(textViewName, progressBar, false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_detail, menu)

        if (!InstantApps.isInstantApp(this)) {
            val shareItem = menu.findItem(R.id.menu_share)

            shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider

            trackSharedJob()

            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE)
            } else {
                setShareIntent()
            }
            return super.onCreateOptionsMenu(menu)
        }

        return false
    }

    private fun trackSharedJob() {
        shareActionProvider?.setOnShareTargetSelectedListener { source, intent ->
            val params = Bundle()
            params.putString("job_id", job?.id)
            params.putString("job_name", job?.position)
            tracker.logEvent("share_job", params)

            return@setOnShareTargetSelectedListener false
        }
    }

    private fun setShareIntent() {

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_logo_400x200)

        try {
            val cachePath = File(this.cacheDir, "/images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val imagePath = File(this.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))

        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${textViewName.text} \n\n ${Html.fromHtml(job?.description)}")
        shareActionProvider?.setShareIntent(shareIntent)
    }

    override fun onDestroy() {
        val file = File(contentUri.path)
        if (file.exists()) {
            file.delete()
        }
        super.onDestroy()
    }

    companion object {
        const val WRITE_EXTERNAL_STORAGE = 2
    }
}
