package com.doctortree.doctortree.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class RegistrationDataM(

    @SerializedName("created_at")
    var created_at: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("error")
    var error: Boolean?,

    @SerializedName("id")
    var id: Int?,

    @SerializedName("message")
    var message: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("updated_at")
    var updated_at: String?,


    ) : Parcelable
