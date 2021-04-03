package com.doctortree.doctortree.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MessageListDataM (

    @SerializedName("created_at")
    var created_at : String?,

    @SerializedName("error")
    var error : Boolean?,

    @SerializedName("id")
    var id : Int?,

    @SerializedName("image")
    var image : String?,

    @SerializedName("message")
    var message : String?,

    @SerializedName("receiver_id")
    var receiver_id : String?,

    @SerializedName("receiver_name")
    var receiver_name : String?,

    @SerializedName("sender_id")
    var sender_id : String?,


    @SerializedName("sender_name")
    var sender_name : String?





    ) :Parcelable