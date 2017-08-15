package com.gkzxhn.gank_kotlin.bean.info

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by 方 on 2017/8/14.
 */

data class IntentMap(var mMap: HashMap<Int, Bitmap>?): Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<IntentMap> = object : Parcelable.Creator<IntentMap> {
            override fun createFromParcel(source: Parcel): IntentMap = IntentMap(source)
            override fun newArray(size: Int): Array<IntentMap?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readHashMap(HashMap::class.java.classLoader) as HashMap<Int, Bitmap>
    )

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeMap(mMap)
    }

    override fun describeContents()=0
}