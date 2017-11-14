package br.com.tairoroberto.remoteok.home.model.domain

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by tairo on 14/11/17.
 */
data class JobsResponse(var list: List<Job>) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Job))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(list)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JobsResponse> {
        override fun createFromParcel(parcel: Parcel): JobsResponse {
            return JobsResponse(parcel)
        }

        override fun newArray(size: Int): Array<JobsResponse?> {
            return arrayOfNulls(size)
        }
    }
}