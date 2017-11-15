package com.remoteok.io.app.home.view

import android.widget.ImageView
import com.remoteok.io.app.home.model.domain.Job

/**
 * Created by tairo on 12/12/17.
 */
interface OnClick {
    fun onItemClick(job: Job, imageView: ImageView)
}