package com.doctortree.doctortree.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.NetworkError
import com.doctortree.doctortree.data.MenuDataM
import com.doctortree.doctortree.retrofit.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MenuViewM : ViewModel() {
    private val apiService = ApiService()
    private val disposable = CompositeDisposable()

    var add_opt_res = MutableLiveData<List<MenuDataM>>();

    fun doMenuReq(activity: Activity){

        disposable.add(
            apiService.call
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<List<MenuDataM>>() {
                    override fun onSuccess(model: List<MenuDataM>) {

                        Log.e("model-->", model.toString())

                        model.let {
                            add_opt_res.value = model
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