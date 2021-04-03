package com.doctortree.doctortree.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class LoginDataM(

    @SerializedName("admin")
    var admin: String?,

    @SerializedName("created_at")
    var created_at: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("email_verified_at")
    var email_verified_at: String?,

    @SerializedName("error")
    var error: Boolean?,

    @SerializedName("id")
    var id: Int?,

    @SerializedName("message")
    var message: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("updated_at")
    var updated_at: String?



):Parcelable
