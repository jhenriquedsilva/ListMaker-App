package com.raywenderlich.listmaker

import android.os.Parcel
import android.os.Parcelable

class TaskList(val name: String, val tasks: ArrayList<String> = ArrayList()): Parcelable {

    // A TaskList is created from a Parcel
    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.createStringArrayList()!!)

    override fun describeContents(): Int {
        return 0
    }

    // A Parcel is created from a TaskList
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(tasks)
    }

    companion object CREATOR : Parcelable.Creator<TaskList> {

        override fun createFromParcel(parcel: Parcel): TaskList {
            return TaskList(parcel)
        }

        override fun newArray(size: Int): Array<TaskList?> {
            return arrayOfNulls(size)
        }

    }

}