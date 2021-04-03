package com.doctortree.doctortree.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.bd.ehaquesoft.sweetalert.SweetAlertDialog
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.RegistrationDataM
import com.doctortree.doctortree.request.RegistrationRequestM
import com.doctortree.doctortree.util.Custom_alert
import com.doctortree.doctortree.util.GlobalVeriable
import com.doctortree.doctortree.viewModel.RegistrationViewM
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Registration : AppCompatActivity() {

    //********* Text Input Edit Text ***************//
    private lateinit var edtEmailReg : TextInputEditText
    private lateinit var edtNameReg : TextInputEditText
    private lateinit var edtPassReg : TextInputEditText
    private lateinit var edtConfirmPassReg : TextInputEditText

    //********* Text Input Edit Text Layout***************//
    private lateinit var filledTextField : TextInputLayout
    private lateinit var nameLyt : TextInputLayout
    private lateinit var passLyt : TextInputLayout
    private lateinit var confirmPassLyt : TextInputLayout

    //**************Buttons ************//
    private lateinit var buttonAcount:Button

    //********* Variables *************//
    private lateinit var email : String
    private lateinit var name : String
    private lateinit var pass : String
    private lateinit var confirmPass : String

    //*********** Sweet Alert *********//
    private lateinit var pDialog: SweetAlertDialog

    //*********** View Model *********//
    private lateinit var regViewModel : RegistrationViewM

    //*********** Global Variables **************//

    private lateinit var globalVeriable: GlobalVeriable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initialization()

        buttonAcount.setOnClickListener {
            email = edtEmailReg.text.toString()
            name = edtNameReg.text.toString()
            pass = edtPassReg.text.toString()
            confirmPass = edtConfirmPassReg.text.toString()

            doRegistration()
        }

        regObserver()
    }

    private fun initialization() {

        //********* Text Input Edit Text ***************//
        edtEmailReg = findViewById(R.id.edtEmailReg)
        edtNameReg = findViewById(R.id.edtNameReg)
        edtPassReg = findViewById(R.id.edtPassReg)
        edtConfirmPassReg  = findViewById(R.id.edtConfirmPassReg)

        //********* Text Input Edit Text Layout***************//
        filledTextField = findViewById(R.id.filledTextField)
        nameLyt = findViewById(R.id.nameLyt)
        passLyt = findViewById(R.id.passLyt)
        confirmPassLyt = findViewById(R.id.confirmPassLyt)

        //*********** Buttons *****************//
        buttonAcount = findViewById(R.id.buttonAcount)

        //*********** Sweet Dialog ************//
        pDialog = Custom_alert.showProgressDialog(this)

        //*********** View Model *************//
        regViewModel = ViewModelProvider(this).get(RegistrationViewM::class.java)

        //************ Global Variables **********//
        globalVeriable = this.applicationContext as GlobalVeriable


    }

    private fun doRegistration(){
        val model = RegistrationRequestM()

        model.email = ""+edtEmailReg.text.toString()
        model.password = ""+edtPassReg.text.toString()
        model.name = ""+edtNameReg.text.toString()
        model.password_confirmation = ""+edtConfirmPassReg.text.toString()

        this.let { regViewModel.doRegistration(model,it) }

        pDialog.show()
    }

    private fun regObserver(){
        regViewModel.registration.observe(
            this,
            {
                pDialog.dismiss()
                it?.let {
                    pDialog.dismiss()

                    if (it.error?.equals(true)!!){
                        Custom_alert.showErrorMessage(this,it.message)
                    }else{
                        val model = RegistrationDataM(
                            it.created_at ,
                            it.email,
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
                            Custom_alert.showSuccessMessage(this,"Registration Successful")
                        }
                    }


                }
            }
        )
    }
}