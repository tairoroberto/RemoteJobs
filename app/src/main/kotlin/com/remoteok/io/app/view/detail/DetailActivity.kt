package com.remoteok.io.app.view.detail

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
import com.remoteok.io.app.BuildConfig
import com.remoteok.io.app.R
import com.remoteok.io.app.model.Job
import com.remoteok.io.app.utils.extension.loadImage
import com.remoteok.io.app.viewmodel.detail.DetailViewModel
import com.remoteok.io.app.viewmodel.detail.DetailViewModelFactory
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetailActivity : AppCompatActivity() {

    private var shareActionProvider: ShareActionProvider? = null

    private lateinit var job: Job

    private lateinit var contentUri: Uri

    private val viewModel by lazy {
        ViewModelProviders.of(this, DetailViewModelFactory()).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setAnimation()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        imageViewLogo.isDrawingCacheEnabled = true
        job = intent.getParcelableExtra("job")

        showJob()

        fab.setOnClickListener { showAlertDialog(job) }
        imageBack.setOnClickListener {
            setAnimation()
            finish()
        }

        contentUri = Uri.EMPTY
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

    private fun showJob() {
        imageViewLogo.loadImage(job.logo, progressImage, false)

        toolbar_layout.title = job.position
        textViewName.text = job.position


        val description =
                "<html>" +
                        "    <head>" +
                        "        <meta charset=\"utf-8\" />" +
                        "    </head>" +
                        "    <body bgcolor=\"#fafafa\"> ${job.description} </body>" +
                        "</html>"

        textViewDescription.loadData(description, "text/html", "UTF-8")
        textViewReleaseDate.text = formatDate(job.date)
    }

    private fun formatDate(date: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SS:SS", Locale.ENGLISH)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return format.format(sdf.parse(date))
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

    private fun setShareIntent() {

        val bitmap = if (imageViewLogo.drawingCache != null) {
            imageViewLogo.drawingCache
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.ic_logo_400x200)
        }

        try {
            val cachePath = File(this.cacheDir, "/images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.png") // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${textViewName.text} \n\n ${Html.fromHtml(job.description)}")
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
