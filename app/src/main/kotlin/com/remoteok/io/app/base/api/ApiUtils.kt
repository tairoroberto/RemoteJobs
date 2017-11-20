package com.remoteok.io.app.base.api

import android.app.Activity
import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.remoteok.io.app.base.extension.isConected
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by tairo on 11/12/17.
 */
object ApiUtils {
    private val URL_BASE = "https://remoteok.io/"

    fun getApiService(context: Context?): Api? =
            RetrofitClient.getRetrofit(context, URL_BASE)?.create(Api::class.java)
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getRetrofit(context: Context?, baseUrl: String): Retrofit? {
        if (retrofit == null) {

            val httpCacheDirectory = File(context?.cacheDir, "responses")
            val cacheSize = 10 * 1024 * 1024 // 10 MiB
            val cache = Cache(httpCacheDirectory, cacheSize.toLong())

            val client = OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .addNetworkInterceptor(getRewriteCacheControlInterceptor((context as Activity).isConected()))
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .cache(cache)
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

fun getRewriteCacheControlInterceptor(isNetworkAvailable: Boolean): Interceptor {
    return Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())

        if (isNetworkAvailable) {
            val maxAge = 60 // read from cache for 1 minute
            originalResponse?.newBuilder()
                    ?.header("Cache-Control", "public, max-age=" + maxAge)
                    ?.build()
        } else {
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build()
        }
    }
}