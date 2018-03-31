package com.remoteok.io.app.utils.extension

import android.os.Build
import android.text.Html
import android.widget.TextView


fun TextView.textHtml(text: String?){
    this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(text)
    }
}