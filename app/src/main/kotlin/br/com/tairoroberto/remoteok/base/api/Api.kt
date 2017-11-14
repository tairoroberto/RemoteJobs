package com.tairoroberto.nextel.base.api


import br.com.tairoroberto.remoteok.home.model.domain.JobsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by tairo on 11/10/17 12:23 AM.
 */
interface Api {

    @GET("remote-jobs.json")
    fun getAll(): Observable<JobsResponse>

    @GET("{path}")
    fun search(@Path("path") path: String): Observable<JobsResponse>
}