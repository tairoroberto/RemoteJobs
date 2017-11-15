package com.remoteok.io.app.base.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId

/**
 * Created by tairo on 9/8/17.
 */
class CustomFirebaseInstaceIdService: FirebaseInstanceIdService() {
    private val TAG = "LOG"

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.i(TAG, "onTokenRefresh: " + refreshedToken)

        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {

    }
}