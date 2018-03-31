package com.remoteok.io.app.model

import com.google.gson.annotations.SerializedName

data class StatisticsResponse(@SerializedName("date")
                              val date: String = "",
                              @SerializedName("items")
                              val items: List<Statistic>?)