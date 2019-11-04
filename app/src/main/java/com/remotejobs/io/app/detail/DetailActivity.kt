package com.remotejobs.io.app.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.transition.ChangeBounds
import android.view.Menu
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.MenuItemCompat
import com.google.android.instantapps.InstantApps
import com.google.firebase.analytics.FirebaseAnalytics
import com.remotejobs.io.app.BuildConfig
import com.remotejobs.io.app.R
import com.remotejobs.io.app.model.Job
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.noButton
import org.jetbrains.anko.okButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DetailActivity : AppCompatActivity() {

    private var shareActionProvider: ShareActionProvider? = null

    private var job: Job? = Job()

    private lateinit var contentUri: Uri

    private lateinit var tracker: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportPostponeEnterTransition()

        tracker = FirebaseAnalytics.getInstance(this@DetailActivity)

        job = intent?.getParcelableExtra("job")

        showJob()

        btnApply.setOnClickListener { showAlertDialog() }
        close.setOnClickListener { onBackPressed() }

        content.setOnClickListener { onBackPressed() }

        contentUri = Uri.EMPTY
        val params = Bundle()
        params.putString("job_name", job?.position)
        tracker.logEvent("detail", params)
    }

    private fun setAnimation() {
        val changeBounds = ChangeBounds()
        changeBounds.duration = 2000
        window.sharedElementExitTransition = changeBounds
    }

    private fun showAlertDialog() {
        alert {
            title = "Apply for this job"
            message = "Position: ${job?.position}\n" +
                    "Company: ${job?.company}\n" +
                    "Created at: ${job?.date}"

            noButton {}
            positiveButton("Apply") {
                applyJob()
            }

        }.show()
    }

    private fun applyJob() {

        when {
            job?.urlApply?.startsWith("http") == true -> {
                browse(job?.urlApply.toString())
                trackApplyedJob()
            }

            job?.urlApply?.contains("mailto:") == true -> {
                startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(job?.urlApply)))
                trackApplyedJob()
            }

            job?.urlApply?.contains("javascript:") == true ->
                alert {
                    title = "Alert"
                    message =
                        "'This job post is older than 90 days and the position is probably filled. Try applying to jobs posted recently instead."
                    okButton { }
                }.show()

            else -> {
                browse(job?.urlApply.toString())
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

        Picasso.get()
            .load(if (job?.logo == "") "http://" else job?.logo)
            .noFade()
            .placeholder(R.drawable.ic_launcher_web)
            .error(R.drawable.ic_launcher_web)
            .into(imageViewLogo, object : Callback {
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }

                override fun onError(e: Exception?) {
                    supportStartPostponedEnterTransition()
                }
            })

        textViewName.text = job?.position

        val index = job?.description?.indexOf("<p style=\"text-align:center\">See more jobs") as Int

        val data = if (index >= 0) {
            job?.description?.removeRange(index, job?.description?.length as Int) + "</div>"
        } else {
            job?.description
        }

        textViewReleaseDate.text = job?.date
        textViewCompany.text = job?.company
        webiewViewDescription.loadData(
            "<body bgcolor=\"#fafafa\">$data</body>",
            "text/html",
            "UTF-8"
        )
        webiewViewDescription.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = GONE
                // fab.visibility = VISIBLE
            }
        }
        webiewViewDescription.isHorizontalScrollBarEnabled = true
        webiewViewDescription.setOnTouchListener { _, _ -> true }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_detail, menu)

        if (!InstantApps.isInstantApp(this)) {
            val shareItem = menu.findItem(R.id.menu_share)

            shareActionProvider =
                MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider?

            trackSharedJob()

            val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE
                )
            } else {
                setShareIntent()
            }
            return super.onCreateOptionsMenu(menu)
        }

        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setShareIntent()
        }
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

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_logo)

        try {
            val cachePath = File(this.cacheDir, "/images")
            cachePath.mkdirs()
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val imagePath = File(this.cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        contentUri =
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))

        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${textViewName.text} \n\n ${Html.fromHtml(job?.description)}"
        )
        shareActionProvider?.setShareIntent(shareIntent)
    }

    override fun onBackPressed() {
        webiewViewDescription.visibility = GONE
        super.onBackPressed()
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
