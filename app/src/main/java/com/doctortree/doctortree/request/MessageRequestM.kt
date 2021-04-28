package com.doctortree.doctortree.request

import okhttp3.MultipartBody

class MessageRequestM {
    var sender_id: String? = null//1 for insert 2 for update
    var receiver_id: String? = null
    var message: String? = null
    //var parts: List<MultipartBody.Part?>? = null
}