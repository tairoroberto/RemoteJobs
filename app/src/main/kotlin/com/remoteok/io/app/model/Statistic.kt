package com.remoteok.io.app.model

import com.google.gson.annotations.SerializedName

data class Statistic(@SerializedName("count")
                     var count: String = "",
                     @SerializedName("tag")
                     var tag: String = "",
                     @SerializedName("percent")
                     var percent: String = "")