package com.remotejobs.io.app.utils.extension

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.remotejobs.io.app.R
import com.squareup.picasso.*
import java.lang.Exception


/**
 * Created by tairo on 11/12/17.
 */

fun ImageView.loadImage(url: String?, progress: ProgressBar?) {

    progress?.visibility = View.VISIBLE

    if (url.isNullOrBlank()) {
        progress?.visibility = View.GONE
        return
    }

    Picasso.get()
            .load(url)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .transform(BitmapTransform(1024, 768))
            .resize(512, 512)
            .into(this, object : Callback {

                override fun onSuccess() {
                    progress?.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    progress?.visibility = View.VISIBLE
                    //Try again online if cache failed
                    Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).error(R.drawable.ic_logo_400x200)
                            .transform(BitmapTransform(1024, 768))
                            .resize(512, 512)
                            .into(this@loadImage, object : Callback {

                                override fun onSuccess() {
                                    progress?.visibility = View.GONE
                                }

                                override fun onError(e: Exception?) {
                                    progress?.visibility = View.GONE
                                }
                            })
                }
            })
}

internal class BitmapTransform(private val maxWidth: Int, private val maxHeight: Int) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val targetWidth: Int
        val targetHeight: Int
        val aspectRatio: Double

        if (source.width > source.height) {
            targetWidth = maxWidth
            aspectRatio = source.height.toDouble() / source.width.toDouble()
            targetHeight = (targetWidth * aspectRatio).toInt()
        } else {
            targetHeight = maxHeight
            aspectRatio = source.width.toDouble() / source.height.toDouble()
            targetWidth = (targetHeight * aspectRatio).toInt()
        }

        val result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false)
        if (result != source) {
            source.recycle()
        }
        return result
    }

    override fun key(): String {
        return maxWidth.toString() + "x" + maxHeight
    }
}