package br.com.tairoroberto.remoteok.home.view

import android.widget.ImageView
import br.com.tairoroberto.remoteok.home.model.domain.Job

/**
 * Created by tairo on 12/12/17.
 */
interface OnClick {
    fun onItemClick(job: Job, imageView: ImageView)
}