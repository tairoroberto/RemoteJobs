package com.remoteok.io.app.utils.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar


/**
 * Created by tairo on 11/12/17.
 */
fun Activity.showProgress(form: View?, progressBar: ProgressBar?, show: Boolean) {
    val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    form?.visibility = if (show) View.GONE else View.VISIBLE
    form?.animate()
            ?.setDuration(shortAnimTime)
            ?.alpha((if (show) 0 else 1).toFloat())
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    form.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

    progressBar?.visibility = if (show) View.VISIBLE else View.GONE
    progressBar?.animate()
            ?.setDuration(shortAnimTime)
            ?.alpha((if (show) 1 else 0).toFloat())
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progressBar.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
}

fun Activity.showSnackBarError(view: View?, msg: String) {
    val snackbar: Snackbar = Snackbar.make(view as View, msg, Snackbar.LENGTH_LONG)
            .setAction("OK", null)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
    snackbar.show()
}

fun Activity.isConected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}

fun Activity.hideSoftKeyboard() {
    val view = currentFocus
    if (view != null) {
        (getSystemService(Context.INPUT_METHOD_SERVICE)
                as? InputMethodManager)?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showSoftKeyboard() {
    val view = currentFocus
    if (view != null) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(view, 0)
    }
}