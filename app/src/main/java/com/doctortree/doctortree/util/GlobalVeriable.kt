package com.doctortree.doctortree.util

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class GlobalVeriable : Application(){

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    var gardenType : String?=""
    var menuId : String?=""
    var id : String?=""
    var name : String?=""
    var email : String?=""

    var context: Context? = null
}