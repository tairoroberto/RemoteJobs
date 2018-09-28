package com.remotejobs.io.app.utils.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.browse


/**
 * Created by tairo on 11/12/17.
 */
fun Context.showProgress(form: View?, progressBar: ProgressBar?, show: Boolean) {
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

fun Context.showSnackBarError(view: View?, msg: String) {
    val snackbar: Snackbar = Snackbar.make(view as View, msg, Snackbar.LENGTH_LONG)
            .setAction("OK", null)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
    snackbar.show()
}

fun Context.isConected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}

fun Context.hideSoftKeyboard() {
    val view = (this as Activity).currentFocus
    if (view != null) {
        (getSystemService(Context.INPUT_METHOD_SERVICE)
                as? InputMethodManager)?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.showSoftKeyboard() {
    val view = (this as Activity).currentFocus
    if (view != null) {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(view, 0)
    }
}

fun Context.removeUnicodeCharacters(data: String): String {

    return data
            .replace("\\u00E2\\u0080\\u0099", "'")
            .replace("\\u00e2\\u0080\\u0099", "'")
            .replace("\\u00E2\\u0080\\u009C", "'")
            .replace("\\u00e2\\u0080\\u009c", "'")
            .replace("\\u00E2\\u0080\\u009D", "'")
            .replace("\\u00e2\\u0080\\u009d", "'")
            .replace("\\u00E2\\u0080\\u0093", "'")
            .replace("\\u00e2\\u0080\\u0093", "'")
            .replace("\\u00E2\\u0082\\u00AC", "€")
            .replace("\\u00e2\\u0082\\u00aC", "€")
            .replace("\\u00c3\\u00a9", "é")
            .replace("\\u00C3\\u00A9", "é")
            .replace("\\u00C3\\u00A0", "à")
            .replace("\\u00c3\\u00a0", "à")
            .replace("&#x2019;", "'")
            .replace("&#xA0;", " ")
            .replace("&amp;", "&")
}

fun Context.launchPlayStore() {
    browse("https://play.google.com/store/apps/details?id=com.remotejobs.io.app")
}