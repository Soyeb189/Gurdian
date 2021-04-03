package com.doctortree.doctortree.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.NetworkError
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.data.MenuDataM
import com.doctortree.doctortree.data.MessageListDataM
import com.doctortree.doctortree.request.DiesesListRequestM
import com.doctortree.doctortree.request.MessageListRequestM
import com.doctortree.doctortree.retrofit.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MessageListViewM : ViewModel() {
    private val apiService = ApiService()
    private val disposable = CompositeDisposable()

    var messageList = MutableLiveData<List<MessageListDataM>>();

    fun getMessageList(reqmodel:MessageListRequestM ,activity: Activity){

        disposable.add(
            apiService.getMessage(reqmodel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<List<MessageListDataM>>() {
                    override fun onSuccess(model: List<MessageListDataM>) {

                        Log.e("model-->", model.toString())

                        model.let {
                            messageList.value = model
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("onError--->", "onError--" + e.toString())
                        //NetworkError.(activity, e)
                        e.printStackTrace()
                    }

                })
        )


    }
}