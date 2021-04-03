package com.doctortree.doctortree.viewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.NetworkError
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.data.LoginDataM
import com.doctortree.doctortree.data.MenuDataM
import com.doctortree.doctortree.data.RegistrationDataM
import com.doctortree.doctortree.request.DiesesListRequestM
import com.doctortree.doctortree.request.LoginRequestM
import com.doctortree.doctortree.request.RegistrationRequestM
import com.doctortree.doctortree.retrofit.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LoginViewM : ViewModel() {
    private val apiService = ApiService()
    private val disposable = CompositeDisposable()

    var login = MutableLiveData<LoginDataM>();

    fun doLogin(model:LoginRequestM ,activity: Activity){

        disposable.add(
            apiService.doLogin(model)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<LoginDataM>() {
                    override fun onSuccess(model: LoginDataM) {

                        Log.e("model-->", model.toString())

                        model.let {
                            login.value = model
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