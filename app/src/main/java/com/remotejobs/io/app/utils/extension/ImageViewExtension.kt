package com.remotejobs.io.app.utils.extension

import android.graphics.Bitmap
import android.widget.ImageView
import com.remotejobs.io.app.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation


/**
 * Created by tairo on 11/12/17.
 */

fun ImageView.loadImage(url: String?) {

    if (url.isNullOrBlank()) {
        return
    }

    Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_launcher_web)
            .error(R.drawable.ic_launcher_web)
            .into(this)
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