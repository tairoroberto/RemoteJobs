/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.remotejobs.io.app.utils.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat

class ActivityLaunchHelper {

    companion object {

        private const val URL_BASE = "https://remotejobs.trmamobile.com"
        private const val URL_HOME = "$URL_BASE/home"
        private const val URL_SIGNIN = "$URL_BASE/signin"
        private const val URL_HIGHESTPAID = "$URL_BASE/highestpaid"
        private const val URL_COMPANIES = "$URL_BASE/companies"

        private var currentTag = ""

        fun launchActivity(activity: Activity, intent: Intent, options: ActivityOptionsCompat? = null, tag: String) {

            if (tag != currentTag) {
                currentTag = tag

                if (options == null) {
                    activity.startActivity(intent)
                } else {
                    ActivityCompat.startActivity(activity, intent, options.toBundle())
                }

                if (intent.dataString == URL_HOME){
                    activity.finishAfterTransition()
                }
            }
        }

        fun homeIntent(context: Context? = null) = baseIntent(URL_HOME, context)

        fun highestpaidIntent(context: Context? = null) = baseIntent(URL_HIGHESTPAID, context)

        fun companiesIntent(context: Context? = null) = baseIntent(URL_COMPANIES, context)


        private fun baseIntent(url: String, context: Context? = null): Intent {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .addCategory(Intent.CATEGORY_DEFAULT)
                    .addCategory(Intent.CATEGORY_BROWSABLE)

            if (context != null) {
                intent.`package` = context.packageName
            }

            return intent
        }
    }
}
