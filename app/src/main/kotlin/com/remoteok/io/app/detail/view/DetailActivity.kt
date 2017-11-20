package com.remoteok.io.app.detail.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.text.Html
import android.transition.ChangeBounds
import android.view.Menu
import com.remoteok.io.app.R
import com.remoteok.io.app.base.extension.loadImage
import com.remoteok.io.app.base.extension.showSnackBarError
import com.remoteok.io.app.detail.contract.DetailContract
import com.remoteok.io.app.detail.presenter.DetailPresenter
import com.remoteok.io.app.home.model.domain.Job

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), DetailContract.View {

    private var shareActionProvider: ShareActionProvider? = null
    private val presenter: DetailContract.Presenter? by lazy {
        DetailPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        presenter?.attachView(this)

        imageView.isDrawingCacheEnabled = true
        val job = intent.getParcelableExtra<Job>("job")

        show(job)

        fab.setOnClickListener { showAlertDialog(job) }
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val changeBounds = ChangeBounds()
            changeBounds.duration = 2000
            window.sharedElementExitTransition = changeBounds
        }
    }

    private fun showAlertDialog(job: Job) {
        alert {
            title = "Apply for this job"
            message = "Position: ${job.position}\n" +
                      "Company: ${job.company}\n" +
                      "Created at: ${formatDate(job.date)}"

            noButton {}
            yesButton {
                browse("https://remoteok.io/l/${job.id}")
            }

        }.show()
    }


    override fun show(job: Job?) {
        imageView.loadImage(job?.logo, progressImage)

        toolbar_layout.title = job?.position
        textViewName.text = job?.position
        textViewOverview.text = Html.fromHtml(job?.description)
        textViewReleaseDate.text = formatDate(job?.date)
    }

    private fun formatDate(date: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SS:SS", Locale.ENGLISH)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return format.format(sdf.parse(date))
    }

    override fun showSnackBarError(msg: String) {
        showSnackBarError(fab, msg)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_detail, menu)
        val shareItem = menu.findItem(R.id.menu_share)

        shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE)
        } else {
            setShareIntent()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty()) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setShareIntent()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setShareIntent() {
        if (shareActionProvider != null) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"

            val bitmap = imageView.drawingCache

            val bitmapPath = Images.Media.insertImage(contentResolver, bitmap, "image_detail", null)
            val bitmapUri = Uri.parse(bitmapPath)

            shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "${textViewName.text} \n\n ${textViewOverview.text}")
            shareActionProvider?.setShareIntent(shareIntent)
        }
    }

    override fun getContext(): Context = this

    override fun getActivity(): Activity? = this

    override fun onDestroy() {
        presenter?.detachView()
        super.onDestroy()
    }

    companion object {
        val WRITE_EXTERNAL_STORAGE = 2
    }
}
