package com.doctortree.doctortree.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bd.ehaquesoft.sweetalert.SweetAlertDialog
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.data.SolutionListDataM
import com.doctortree.doctortree.request.DiesesListRequestM
import com.doctortree.doctortree.request.SolutionListRequestM
import com.doctortree.doctortree.util.Custom_alert
import com.doctortree.doctortree.util.GlobalVeriable
import com.doctortree.doctortree.viewModel.SolutionListViewM
import com.google.android.material.navigation.NavigationView

class Solution : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //*********** Toolbar ************//
    private lateinit var toolbar: Toolbar

    //*********** Drawer *************//
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    //************ Global Variable ***********//
    private lateinit var globalVeriable: GlobalVeriable

    private lateinit var tvDescription : TextView
    private lateinit var tvFertilizer : TextView

    private lateinit var diesesId : String
    private lateinit var diesesType : String
    private lateinit var quantity : String
    private lateinit var age : String
    private lateinit var menu_id : String

    //************ Solution View Model ***********************//
    private lateinit var solutionListViewM:SolutionListViewM
    private lateinit var solutionList : ArrayList<SolutionListDataM>

    private lateinit var pDialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solution_drawer)

        initialization()

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Solution"

        //************ For Drawer *****************///
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        diesesId=intent.getStringExtra("DIESES_ID").toString()
        diesesType=intent.getStringExtra("DIESES_TYPE").toString()
        quantity= intent.getStringExtra("QUANTITY").toString()
        age=intent.getStringExtra("AGE").toString()
        menu_id = globalVeriable.menuId.toString()

        doSolutionList()

        doSolutionListObserver()
    }

    private fun initialization() {
        /************ Toolbar ****************/
        toolbar = findViewById(R.id.toolbar)

        //************ Drawer ****************//
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        globalVeriable = this.applicationContext as GlobalVeriable

        tvDescription = findViewById(R.id.tvDescription)
        tvFertilizer = findViewById(R.id.tvFertilizer)

        solutionListViewM = ViewModelProvider(this).get(SolutionListViewM::class.java)
        solutionList = ArrayList<SolutionListDataM>()

        //************ Alert Dialog **********//
        pDialog = Custom_alert.showProgressDialog(this)
    }

    private fun doSolutionList() {
        val model = SolutionListRequestM()
        model.menu_id = menu_id
        model.disease_id = diesesId
        model.type = diesesType
        pDialog.show()

        this.let { it1 -> solutionListViewM.doSolutionReq(model, it1) }
    }

    private fun doSolutionListObserver() {

        solutionListViewM.solutionList.observe(
            this,
            androidx.lifecycle.Observer {


                it?.let {
                    pDialog.dismiss()

                    try {
                        solutionList.clear()
                    } catch (e: Exception) {
                    }

                    solutionList = ArrayList<SolutionListDataM>()

                    for (i in 0 until it.size) {

                        val model = SolutionListDataM(

                            it.get(i).id,
                            it.get(i).amount.toString(),
                            it.get(i).description.toString(),
                            it.get(i).pesticides.toString(),
                            it.get(i).fertilizer.toString(),
                            it.get(i).error

                        )

                        solutionList.add(model)

                        Log.e("Size", solutionList.size.toString())

                    }

                    val solution = solutionList?.filter {it.amount.equals(quantity)}.map { it.description }

                    val fertilezer = solutionList?.filter {it.amount.equals(quantity)}.map { it.fertilizer }

                    tvDescription.setText(fertilezer[0])
                    tvFertilizer.setText(solution[0])

                }

            })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var id : Int = item.itemId

        when(id){
            R.id.account ->
                Toast.makeText(this,"Account", Toast.LENGTH_SHORT).show()
            R.id.help ->
                Toast.makeText(this,"Help", Toast.LENGTH_SHORT).show()
            R.id.about ->
                Toast.makeText(this,"About", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}