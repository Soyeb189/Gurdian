package com.doctortree.doctortree.data

import com.google.gson.annotations.SerializedName

class MenuDataM (
    @SerializedName("id")
    var id: Int?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("status")
    var status: String?,

    @SerializedName("description")
    var description: String?,

    @SerializedName("image")
    var image: String?,

    @SerializedName("error")
    var error: Boolean?
)