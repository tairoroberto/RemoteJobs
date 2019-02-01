package com.remotejobs.io.app.model

import com.google.gson.annotations.SerializedName

data class HighestPaidRespose(@SerializedName("success")
                              var date: String = "",
                              @SerializedName("items")
                              var items: List<HighestPaid>?)