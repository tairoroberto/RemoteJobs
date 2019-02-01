package com.remotejobs.io.app.model

import com.google.gson.annotations.SerializedName

data class StatisticsResponse(@SerializedName("success")
                              val date: String = "",
                              @SerializedName("items")
                              val items: List<Statistic>?)