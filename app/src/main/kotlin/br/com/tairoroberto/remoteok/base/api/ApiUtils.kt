package com.tairoroberto.nextel.base.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by tairo on 11/12/17.
 */
object ApiUtils {
    private val URL_BASE = "https://remoteok.io/"

    fun getApiService(): Api? {
        return RetrofitClient.getRetrofit(URL_BASE)?.create(Api::class.java)
    }
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getRetrofit(baseUrl: String): Retrofit? {
        if (retrofit == null) {

            val client = OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .build()

            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }

        return retrofit
    }
}