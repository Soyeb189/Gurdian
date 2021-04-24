package com.doctortree.doctortree.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bd.ehaquesoft.sweetalert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.MenuDataM
import com.doctortree.doctortree.util.Custom_alert
import com.doctortree.doctortree.util.GlobalVeriable
import com.doctortree.doctortree.util.JWTUtils
import com.doctortree.doctortree.viewModel.MenuViewM
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{

    //*********** Toolbar ************//
    private lateinit var toolbar: Toolbar

    //*********** Sweet Alert *********//
    private lateinit var pDialog: SweetAlertDialog

    //*********** Drawer *************//
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var toggle:ActionBarDrawerToggle

    //*********** View Model ************//
    private lateinit var menuViewM: MenuViewM

    //*********** Menu List *************//
    private lateinit var menuList : ArrayList<MenuDataM>

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var globalVeriable: GlobalVeriable

    private  var jwtString : String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvZGVtby5zaGFpa290LmNvbVwvYXBpXC92MS4wXC9sb2dpbiIsImlhdCI6MTYxMzgwMjA5MCwiZXhwIjoxNjEzODA1NjkwLCJuYmYiOjE2MTM4MDIwOTAsImp0aSI6Ik9RY1p5UHAyUERwaHg1OGIiLCJzdWIiOjUsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.cqryDndEwSbqgO6JnCFUeHbJhl7wsVvMyay7LB-HtH4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigation_drawer)


        initialization()

        JWTUtils.decoded(jwtString)
        Toast.makeText(this,JWTUtils.decoded(jwtString).toString(),Toast.LENGTH_SHORT).show()

        setSupportActionBar(toolbar)
        supportActionBar?.title = "GARDIAN"
        //pDialog.show()

        //************ For Drawer *****************///
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        doDailyTransactionNoTransfer()
        dailyTransferTransactionListObserver()
    }


    private fun doDailyTransactionNoTransfer() {

        pDialog.show()

        this.let { it1 -> menuViewM.doMenuReq(it1) }
    }

    private fun dailyTransferTransactionListObserver() {

        menuViewM.add_opt_res.observe(
            this,
            androidx.lifecycle.Observer {



                it?.let {
                    pDialog.dismiss()

                    try {
                        menuList.clear()
                    } catch (e: Exception) {
                    }

                    menuList = ArrayList<MenuDataM>()

                    for (i in 0 until it.size) {

                        val model = MenuDataM(

                            it.get(i).id,
                            it.get(i).name.toString(),
                            it.get(i).status.toString(),
                            it.get(i).description.toString(),
                            it.get(i).image.toString(),
                            it.get(i).error

                        )

                        menuList.add(model)

                        Log.e("Size",menuList.size.toString())

                    }

                    viewManager = LinearLayoutManager(this)
                    viewAdapter =
                        MenuListAdapter(
                            menuList,
                            this,
                            object :
                                MenuListAdapter.OnItemClickListener {
                                override fun onItemClick(item: MenuDataM?) {

                                }
                            })

                    recyclerView = findViewById<RecyclerView>(R.id.rvMenu).apply {
                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        setHasFixedSize(true)

                        viewManager.isAutoMeasureEnabled = false;

                        // use a linear layout manager
                        layoutManager = viewManager

                        // specify an viewAdapter (see also next example)
                        adapter = viewAdapter

                    }
                }

            })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }

    private fun initialization() {
        //************ Toolbar ****************//
        toolbar = findViewById(R.id.toolbar)

        //************ Alert Dialog **********//
        pDialog = Custom_alert.showProgressDialog(this)

        //************ Drawer ****************//
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        //************* View Model **************//
        menuViewM = ViewModelProviders.of(this).get(MenuViewM::class.java)

        //************ Menu List ****************//
        menuList = ArrayList<MenuDataM>()

        globalVeriable = this.applicationContext as GlobalVeriable

    }


    //***************** Adapter Class start here *********************//

    class MenuListAdapter(
        private val menuList: ArrayList<MenuDataM>,
        private val context: Context,
        listenerInit: OnItemClickListener

    ) : RecyclerView.Adapter<MenuListAdapter.Adapter>() {

        private lateinit var globalVeriable: GlobalVeriable


        var menuListFilter = ArrayList<MenuDataM>()
        var listener: OnItemClickListener

        init {
            menuListFilter = menuList
            listener = listenerInit

            globalVeriable = context.applicationContext as GlobalVeriable
        }

        interface OnItemClickListener {
            fun onItemClick(item: MenuDataM?)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): Adapter {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.menu_card, parent, false)
            return Adapter(view)
        }

        override fun onBindViewHolder(
            holder: Adapter,
            position: Int
        ) {
            val menuList = menuListFilter[position]

           if (menuList.status.equals("1" ) && menuList.error == false){
               holder.menu_name.text = menuList.name

               Glide.with(context)
                   .load(menuList.image)
                   .centerCrop()                        //optional
                   .into(holder.menu_img);
           }

        }

        override fun getItemCount(): Int {
            return menuListFilter.size
        }

        inner class Adapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var menu_img: ImageView
            var menu_name: TextView
            var menu_card: CardView
            var btnChad : Button
            var btnBaranda : Button
            init {
                menu_img = itemView.findViewById(R.id.menu_img)
                menu_name = itemView.findViewById(R.id.menu_name)
                menu_card = itemView.findViewById(R.id.menu_card)
                btnChad = itemView.findViewById(R.id.btnChad)
                btnBaranda = itemView.findViewById(R.id.btnBaranda)


                menu_card.setOnClickListener {
                    val position = adapterPosition

                    if (position != RecyclerView.NO_POSITION) {
                        val selectedList: MenuDataM =
                            menuListFilter[position]
                        listener.onItemClick(selectedList)
                    } else {
                        Toast.makeText(context, "No Position", Toast.LENGTH_SHORT).show()
                    }
                }

                btnChad.setOnClickListener {
                    val position = adapterPosition

                    if (position != RecyclerView.NO_POSITION) {
                        val selectedList: MenuDataM =
                            menuListFilter[position]
                        listener.onItemClick(selectedList)
                        //Toast.makeText(context,selectedList.id.toString(),Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context,"Chad",Toast.LENGTH_SHORT).show()
                        val intent = Intent(context,Dieses::class.java)
                        intent.putExtra("id",selectedList.id.toString())
                        intent.putExtra("type","C")
                        globalVeriable.gardenType = "C"
                        globalVeriable.menuId = selectedList.id.toString()
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "No Position", Toast.LENGTH_SHORT).show()
                    }
                }

                btnBaranda.setOnClickListener {
                    val position = adapterPosition

                    if (position != RecyclerView.NO_POSITION) {
                        val selectedList: MenuDataM =
                            menuListFilter[position]
                        listener.onItemClick(selectedList)
                        //Toast.makeText(context,selectedList.id.toString(),Toast.LENGTH_SHORT).show()
                        //Toast.makeText(context,"baranda",Toast.LENGTH_SHORT).show()
                        val intent = Intent(context,Dieses::class.java)
                        intent.putExtra("id",selectedList.id.toString())
                        intent.putExtra("type","B")
                        globalVeriable.gardenType = "B"
                        globalVeriable.menuId = selectedList.id.toString()
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "No Position", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }


    }

    //***************** Adapter Class end here *********************//

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