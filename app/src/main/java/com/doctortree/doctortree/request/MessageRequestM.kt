package com.doctortree.doctortree.request

import okhttp3.MultipartBody
import okhttp3.RequestBody


class MessageRequestM {
    var sender_id: RequestBody? = null//1 for insert 2 for update
    var receiver_id: RequestBody? = null
    var message: RequestBody? = null
    var body: MultipartBody.Part? = null
}