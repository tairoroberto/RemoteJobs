package com.remotejobs.io.app.jobs.domain.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by tairo on 9/8/17.
 */
class CustomFirebaseInstaceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("TAG", "onTokenRefresh: $refreshedToken")

        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {

    }
}