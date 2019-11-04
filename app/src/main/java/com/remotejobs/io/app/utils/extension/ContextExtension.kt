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
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


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
        (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
            view,
            0
        )
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

fun Context.loadJSONFromAsset(path: String): String? {
    var json: String? = null
    try {
        val `is` = assets.open(path)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }

    return json
}

fun Context.calculateDifferenceInDays(start: Date, end: Date, locale: Locale): Long {
    val cal = Calendar.getInstance(locale)

    cal.time = start
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val startTime = cal.timeInMillis

    cal.time = end
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val endTime = cal.timeInMillis

    // calculate the offset if one of the dates is in summer time and the other one in winter time
    val timezone = cal.timeZone
    val offsetStart = timezone.getOffset(startTime)
    val offsetEnd = timezone.getOffset(endTime)
    val offset = offsetEnd - offsetStart

    return TimeUnit.MILLISECONDS.toDays(endTime - startTime + offset)
}

fun Context.getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillies = date2.time - date1.time
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}