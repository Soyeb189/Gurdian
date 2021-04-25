package com.doctortree.doctortree.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bd.ehaquesoft.sweetalert.SweetAlertDialog
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.LoginDataM
import com.doctortree.doctortree.data.RegistrationDataM
import com.doctortree.doctortree.request.LoginRequestM
import com.doctortree.doctortree.request.RegistrationRequestM
import com.doctortree.doctortree.util.Custom_alert
import com.doctortree.doctortree.util.GlobalVeriable
import com.doctortree.doctortree.util.PermissionUtil
import com.doctortree.doctortree.viewModel.LoginViewM
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {

    private lateinit var edtEmail:TextInputEditText
    private lateinit var edtPassword:TextInputEditText

    private lateinit var btnLogin:Button

    private lateinit var loginVM : LoginViewM
    private lateinit var pDialog:SweetAlertDialog

    private lateinit var loginLink:TextView

    //**************** Global Veriable ********************//
    private lateinit var globalVeriable: GlobalVeriable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialization()

        try {
            PermissionUtil.checkPermission(this, this)
        }catch (e:Exception){}

        btnLogin.setOnClickListener {
            doLogin()
        }

        loginLink.setOnClickListener {
            var i = Intent(this,Registration::class.java)
            startActivity(i)
        }

        loginObserver()
    }

    private fun initialization() {

        edtEmail = findViewById(R.id.edtEmailLogin)
        edtPassword = findViewById(R.id.edtPasswordLogin)

        btnLogin = findViewById(R.id.btnLogin)

        loginVM = ViewModelProvider(this).get(LoginViewM::class.java)
        pDialog = Custom_alert.showProgressDialog(this)

        globalVeriable = this.applicationContext as GlobalVeriable

        loginLink = findViewById(R.id.loginLink)
    }

    private fun doLogin(){
        val model = LoginRequestM()

        model.email = ""+edtEmail.text.toString()
        model.password = ""+edtPassword.text.toString()

        this.let { loginVM.doLogin(model,it) }

        pDialog.show()
    }

    private fun loginObserver(){
        loginVM.login.observe(
            this,
            {
                pDialog.dismiss()
                it?.let {
                    pDialog.dismiss()

                    if (it.error?.equals(true)!!){
                        Custom_alert.showErrorMessage(this,it.message)
                    }else{
                        val model = LoginDataM(
                            it.admin,
                            it.created_at,
                            it.email,
                            it.email_verified_at,
                            it.error,
                            it.id,
                            it.message,
                            it.name,
                            it.updated_at
                        )

                        globalVeriable.id = model.id.toString()
                        globalVeriable.name = model.name
                        globalVeriable.email = model.email

                        if (model.error?.equals(false)!!){

                            SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("লগিন সম্পূর্ণ হয়েছে")
                                .setConfirmText("ওকে")
                                .setConfirmClickListener {
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .show()

//                            Custom_alert.showSuccessMessage(this,"লগিন সম্পূর্ণ হয়েছে")
//                            pDialog.confirmText = "লগিন সম্পূর্ণ হয়েছে"
//                            val intent = Intent(this, MainActivity::class.java)
//                            startActivity(intent)
//                            finish()
                        }
                    }


                }
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        pDialog.dismiss()
    }
}