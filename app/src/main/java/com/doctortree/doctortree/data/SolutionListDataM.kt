package com.doctortree.doctortree.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class SolutionListDataM(

    @SerializedName("id")
    var id: Int?,

    @SerializedName("amount")
    var amount: String?,

    @SerializedName("fertilizer")
    var fertilizer: String?,

    @SerializedName("pesticides")
    var pesticides: String?,

    @SerializedName("description")
    var description: String?,

    @SerializedName("error")
    var error: Boolean?
):Parcelable
