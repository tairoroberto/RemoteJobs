package com.remoteok.io.app.base.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar

/**
 * Created by tairo on 11/12/17.
 */
fun Fragment.showProgress(form: View?, progressBar: ProgressBar?, show: Boolean) {
    val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

    form?.visibility = if (show) View.VISIBLE else View.GONE
    form?.animate()
            ?.setDuration(shortAnimTime)
            ?.alpha((if (show) 0 else 1).toFloat())
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    form.visibility = if (show) View.VISIBLE else View.GONE
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

fun Fragment.showSnackBarError(view: View, msg: String) {
    val snackbar: Snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setAction("OK", null)
    snackbar.view.setBackgroundColor(ContextCompat.getColor(activity as Context, android.R.color.holo_red_light))
    snackbar.show()
}

fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}