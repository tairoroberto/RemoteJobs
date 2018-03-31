package com.remoteok.io.app.model

import com.google.gson.annotations.SerializedName

data class HighestPaidRespose(@SerializedName("date")
                              var date: String = "",
                              @SerializedName("items")
                              var items: List<HighestPaid>?)