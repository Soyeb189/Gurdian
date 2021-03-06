package com.doctortree.doctortree.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doctortree.doctortree.data.MessageDataM
import com.doctortree.doctortree.request.MessageRequestM
import com.doctortree.doctortree.request.MessageRequestWithoutImageM
import com.doctortree.doctortree.retrofit.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MessageWithoutImageVM : ViewModel(){

    private val apiService = ApiService()
    private val disposable = CompositeDisposable()
    var message_info = MutableLiveData<MessageDataM>();


    fun doMessage(model: MessageRequestWithoutImageM, activity: Activity){

        disposable.add(apiService.doLrDocUploadWithoutImage(model)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<MessageDataM>() {
                override fun onSuccess(model: MessageDataM) {
                    //pDialog?.hide()

                    Log.e("Upload","Success")

                    Log.e("model-->",model.toString())

                    model?.let {
                        message_info.value = model
                    }

                }

                override fun onError(e: Throwable) {
                    //pDialog?.hide()
                    Log.e("Upload","Failed")
                    Log.e("onError--->","onError--"+e.toString())

                    e.printStackTrace()


                }

            })
        )
    }
}