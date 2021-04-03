package com.doctortree.doctortree.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class DiesesListDataM(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("type")
    var type: String?,

    @SerializedName("image")
    var image: String?,

    @SerializedName("error")
    var error: Boolean?
):Parcelable
