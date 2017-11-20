package com.remoteok.io.app.home.model.domain

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by tairo on 14/11/17.
 */
data class JobsResponse(var list: List<Job>? = ArrayList()) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Job))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<JobsResponse> {
        override fun createFromParcel(parcel: Parcel): JobsResponse = JobsResponse(parcel)

        override fun newArray(size: Int): Array<JobsResponse?> = arrayOfNulls(size)
    }
}