package com.doctortree.doctortree.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.util.Custom_alert
import com.doctortree.doctortree.util.GlobalVeriable
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dieses_details.*

class DiesesDetails : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //*********** Toolbar ************//
    private lateinit var toolbar: Toolbar

    //*********** Drawer *************//
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    //*********** Dieses data model **********//
    private var selectedDieses: DiesesListDataM? = null

    //************ Variables *****************//
    private lateinit var diesesName : String
    private lateinit var diesesImage : String
    private lateinit var diesesId : String
    private lateinit var diesesType : String
    private var selectedButton : Int = 0
    private var selectedButtonAge : Int = 0
    private lateinit var quantity : String
    private lateinit var age : String

    //************ Global Variable ***********//
    private lateinit var globalVeriable: GlobalVeriable

    //************ Variables *****************//
    val quantityOne:Int = 1
    val quantityTwo:Int = 2
    val quantityThree:Int = 3
    val ageOne:Int = 4
    val ageTwo:Int = 5
    val ageThree:Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dieses_details_drawer)

        initialization()

        if (intent.hasExtra("list")){
            selectedDieses =  intent.getParcelableExtra("list")

            diesesName = selectedDieses?.name.toString()
            diesesImage = selectedDieses?.image.toString()
            diesesId = selectedDieses?.id.toString()
            diesesType = selectedDieses?.type.toString()

            tvDiesesName.text = diesesName

            Glide.with(this)
                .load(diesesImage)
                .centerCrop()
                .into(diessesImage)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = diesesName

        //************ For Drawer *****************///
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        btnQuantityOne.setOnClickListener {
            changeButton(quantityOne)
            quantity = "1-5"
        }

        btnQuantityTwo.setOnClickListener {
            changeButton(quantityTwo)
            quantity = "6-10"
        }

        btnQuantityThree.setOnClickListener {
            changeButton(quantityThree)
            quantity = "11-20"
        }

        btnAgeOne.setOnClickListener {
            changeButtonForAGe(ageOne)
            age = "1-5"
        }
        btnAgeTwo.setOnClickListener {
            changeButtonForAGe(ageTwo)
            age = "6-10"
        }
        btnAgeThree.setOnClickListener {
            changeButtonForAGe(ageThree)
            age = "11-20"
        }

        btnSubmit.setOnClickListener {
            when {
                selectedButton == 0 -> {
                    Custom_alert.showWarningMessage(this,"Please Select Quantity")
                }
                selectedButtonAge == 0 -> {
                    Custom_alert.showWarningMessage(this,"Please Select Age")
                }
                else -> {
                    var intent = Intent(this,Solution::class.java)
                    intent.putExtra("QUANTITY",quantity)
                    intent.putExtra("AGE",age)
                    intent.putExtra("DIESES_ID",diesesId)
                    intent.putExtra("DIESES_TYPE",diesesType)
                    startActivity(intent)
                }
            }
        }
//        Toast.makeText(this, selectedDieses?.id.toString(),Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, selectedDieses?.type.toString(),Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,globalVeriable.menuId,Toast.LENGTH_SHORT).show()
    }

    private fun initialization() {
        /************ Toolbar ****************/
        toolbar = findViewById(R.id.toolbar)

        //************ Drawer ****************//
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        globalVeriable = this.applicationContext as GlobalVeriable
    }

    private fun changeButton(what : Int){
        selectedButton = what


        btnQuantityOne.setBackgroundResource(R.drawable.bt_background_rounded_yellow)
        btnQuantityOne.setTextColor(Color.parseColor("#ffffff"))

        btnQuantityTwo.setBackgroundResource(R.drawable.bt_background_rounded_yellow)
        btnQuantityTwo.setTextColor(Color.parseColor("#ffffff"))

        btnQuantityThree.setBackgroundResource(R.drawable.bt_background_rounded_yellow)
        btnQuantityThree.setTextColor(Color.parseColor("#ffffff"))


        when (what) {
            quantityOne -> {
                btnQuantityOne.setBackgroundResource(R.drawable.bt_background_active)
                btnQuantityOne.setTextColor(Color.parseColor("#ffffff"))
            }
            quantityTwo -> {
                btnQuantityTwo.setBackgroundResource(R.drawable.bt_background_active)
                btnQuantityTwo.setTextColor(Color.parseColor("#ffffff"))
            }
            quantityThree -> {
                btnQuantityThree.setBackgroundResource(R.drawable.bt_background_active)
                btnQuantityThree.setTextColor(Color.parseColor("#ffffff"))
            }
        }


    }

    private fun changeButtonForAGe(what : Int){
        selectedButtonAge = what


        btnAgeOne.setBackgroundResource(R.drawable.bt_background_rounded_yellow)
        btnAgeOne.setTextColor(Color.parseColor("#ffffff"))

        btnAgeTwo.setBackgroundResource(R.drawable.bt_background_rounded_yellow)
        btnAgeTwo.setTextColor(Color.parseColor("#ffffff"))

        btnAgeThree.setBackgroundResource(R.drawable.bt_background_rounded_yellow)
        btnAgeThree.setTextColor(Color.parseColor("#ffffff"))

        when (what) {

            ageOne -> {
                btnAgeOne.setBackgroundResource(R.drawable.bt_background_active)
                btnAgeOne.setTextColor(Color.parseColor("#ffffff"))
            }
            ageTwo -> {
                btnAgeTwo.setBackgroundResource(R.drawable.bt_background_active)
                btnAgeTwo.setTextColor(Color.parseColor("#ffffff"))
            }
            ageThree -> {
                btnAgeThree.setBackgroundResource(R.drawable.bt_background_active)
                btnAgeThree.setTextColor(Color.parseColor("#ffffff"))
            }
        }


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