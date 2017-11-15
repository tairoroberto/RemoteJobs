package br.com.tairoroberto.remoteok.base.extension

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import br.com.tairoroberto.remoteok.R
import com.facebook.stetho.common.StringUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


/**
 * Created by tairo on 11/12/17.
 */

fun ImageView.loadImage(url: String?, progress: ProgressBar?) {
    progress?.visibility = View.VISIBLE

    if (url.isNullOrBlank()){
        this.setImageResource(R.drawable.logo)
        progress?.visibility = View.GONE
        return
    }

    Picasso.with(context)
            .load(url)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(this, object : Callback {
                override fun onSuccess() {
                    progress?.visibility = View.GONE
                }

                override fun onError() {
                    progress?.visibility = View.VISIBLE
                    //Try again online if cache failed
                    Picasso.with(context).load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).error(R.drawable.logo)
                            .into(this@loadImage, object : Callback {

                                override fun onSuccess() {
                                    progress?.visibility = View.GONE
                                }

                                override fun onError() {
                                    progress?.visibility = View.GONE
                                }
                            })
                }
            })
}