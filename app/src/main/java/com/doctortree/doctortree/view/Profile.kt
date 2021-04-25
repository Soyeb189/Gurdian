package com.doctortree.doctortree.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.doctortree.doctortree.R
import com.doctortree.doctortree.util.GlobalVeriable
import com.google.android.material.navigation.NavigationView

class Profile : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{

    //*********** Toolbar ************//
    private lateinit var toolbar: Toolbar

    //*********** Drawer *************//
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    //*********** Text View **************//
    private lateinit var tvName : TextView
    private lateinit var tvEmail : TextView


    private lateinit var globalVeriable: GlobalVeriable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_drawer)

        initialization()

        setSupportActionBar(toolbar)
        supportActionBar?.title = "প্রোফাইল"


        //************ For Drawer *****************///
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)


        tvName.setText(globalVeriable.name)
        tvEmail.setText(globalVeriable.email)
    }

    private fun initialization(){
        //************ Toolbar ****************//
        toolbar = findViewById(R.id.toolbar)

        //************ Drawer ****************//
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        //*********** Text View **************//
        tvName  = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)

        globalVeriable = this.applicationContext as GlobalVeriable


    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var id : Int = item.itemId
        var i : Intent

        when(id){
            R.id.account ->{
                i = Intent(this,Profile::class.java)
                startActivity(i)
            }

            R.id.help ->{
                i = Intent(this,Messaging::class.java)
                startActivity(i)
            }

            R.id.about ->{
                i = Intent(this,About::class.java)
                startActivity(i)
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}