package com.remoteok.io.app.home.model.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by tairo on 14/11/17.
 */
data class JobsResponse(
        @SerializedName("date")
        var date: String,

        @SerializedName("items")
        var list: List<Job>? = ArrayList())