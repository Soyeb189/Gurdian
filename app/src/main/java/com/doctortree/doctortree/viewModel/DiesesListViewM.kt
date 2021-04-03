package com.doctortree.doctortree.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.NetworkError
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.data.MenuDataM
import com.doctortree.doctortree.request.DiesesListRequestM
import com.doctortree.doctortree.retrofit.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class DiesesListViewM : ViewModel() {
    private val apiService = ApiService()
    private val disposable = CompositeDisposable()

    var diesesList = MutableLiveData<List<DiesesListDataM>>();

    fun doMenuReq(model:DiesesListRequestM ,activity: Activity){

        disposable.add(
            apiService.getDieses(model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<List<DiesesListDataM>>() {
                    override fun onSuccess(model: List<DiesesListDataM>) {

                        Log.e("model-->", model.toString())

                        model.let {
                            diesesList.value = model
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