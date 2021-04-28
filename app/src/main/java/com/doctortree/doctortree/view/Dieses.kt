package com.doctortree.doctortree.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bd.ehaquesoft.sweetalert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.request.DiesesListRequestM
import com.doctortree.doctortree.util.Custom_alert
import com.doctortree.doctortree.util.GlobalVeriable
import com.doctortree.doctortree.viewModel.DiesesListViewM
import com.google.android.material.navigation.NavigationView

class Dieses : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var id:String
    private lateinit var type:String

    //*********** Toolbar ************//
    private lateinit var toolbar: Toolbar

    //*********** Drawer *************//
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    lateinit var toggle: ActionBarDrawerToggle

    //*********** View Model ************//
    private lateinit var diesesViewM: DiesesListViewM

    //*********** Menu List *************//
    private lateinit var diesesList : ArrayList<DiesesListDataM>

    //*********** Sweet Alert *********//
    private lateinit var pDialog: SweetAlertDialog

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    //************** Global variable **************//
    private lateinit var globalVeriable: GlobalVeriable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dieses_drawer)

        initialization()

        setSupportActionBar(toolbar)
        supportActionBar?.title = "রোগসমূহ"

        //Toast.makeText(this,globalVeriable.id + globalVeriable.name + globalVeriable.email , Toast.LENGTH_SHORT).show()

        //************ For Drawer *****************///
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        id = intent.getStringExtra("id").toString()
        type = intent.getStringExtra("type").toString()

       // Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
        // Toast.makeText(this, type, Toast.LENGTH_SHORT).show()

        doDailyTransactionNoTransfer()
        dailyTransferTransactionListObserver()
    }

    private fun doDailyTransactionNoTransfer() {
        val model = DiesesListRequestM()
        model.id = id
        pDialog.show()

        this.let { it1 -> diesesViewM.doMenuReq(model, it1) }
    }

    private fun dailyTransferTransactionListObserver() {

        diesesViewM.diesesList.observe(
            this,
            androidx.lifecycle.Observer {


                it?.let {
                    pDialog.dismiss()

                    try {
                        diesesList.clear()
                    } catch (e: Exception) {
                    }

                    diesesList = ArrayList<DiesesListDataM>()

                    for (i in 0 until it.size) {

                        val model = DiesesListDataM(

                            it.get(i).id,
                            it.get(i).name.toString(),
                            it.get(i).status.toString(),
                            it.get(i).type.toString(),
                            it.get(i).image.toString(),
                            it.get(i).error

                        )

                        diesesList.add(model)

                        Log.e("Size", diesesList.size.toString())

                    }

                    viewManager = LinearLayoutManager(this)
                    viewAdapter =
                        Dieses.MenuListAdapter(
                            diesesList,
                            this,
                            object :
                                Dieses.MenuListAdapter.OnItemClickListener {
                                override fun onItemClick(item: DiesesListDataM?) {

                                }
                            })

                    recyclerView = findViewById<RecyclerView>(R.id.rvMenu).apply {
                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        setHasFixedSize(true)

                         viewManager.isAutoMeasureEnabled = false;

                        // use a linear layout manager
                        layoutManager = GridLayoutManager(this@Dieses,1)

                        //recyclerView.layoutManager = GridLayoutManager(this@Dieses, 2)

                        // specify an viewAdapter (see also next example)
                        adapter = viewAdapter

                    }
                }

            })
    }

    private fun initialization() {
       diesesViewM = ViewModelProvider(this).get(DiesesListViewM::class.java)
        diesesList = ArrayList<DiesesListDataM>()

        //************ Alert Dialog **********//
        pDialog = Custom_alert.showProgressDialog(this)

        /************ Toolbar ****************/
        toolbar = findViewById(R.id.toolbar)

        //************ Drawer ****************//
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        /**************** Global variable  ****************/

        globalVeriable = this.applicationContext as GlobalVeriable
    }


    //***************** Adapter Class start here *********************//

    class MenuListAdapter(
        private val menuList: ArrayList<DiesesListDataM>,
        private val context: Context,
        listenerInit: OnItemClickListener,

        ) : RecyclerView.Adapter<MenuListAdapter.Adapter>() {


        var menuListFilter = ArrayList<DiesesListDataM>()
        var listener: OnItemClickListener
        var globalVeriable : GlobalVeriable

        init {
            menuListFilter = menuList
            listener = listenerInit
            globalVeriable = context.applicationContext as GlobalVeriable
        }

        interface OnItemClickListener {
            fun onItemClick(item: DiesesListDataM?)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): Adapter {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.dieses_card, parent, false)
            return Adapter(view)
        }

        override fun onBindViewHolder(
            holder: Adapter,
            position: Int,
        ) {
            val menuList = menuListFilter[position]


            if (globalVeriable.gardenType.equals("C")){
                if (menuList.type?.equals("2")!!){
                    holder.menu_card.visibility = View.GONE
                    holder.diesesCardLyt.visibility = View.GONE
                }else{
                    holder.menu_name.text = menuList.name

                    Glide.with(context)
                        .load(menuList.image)
                        .centerCrop()                        //optional
                        .into(holder.menu_img)
                }
            }else if (globalVeriable.gardenType.equals("B")){
                if (menuList.type?.equals("1")!!){
                    holder.menu_card.visibility = View.GONE
                    holder.diesesCardLyt.visibility = View.GONE
                }else{
                    holder.menu_name.text = menuList.name

                    Glide.with(context)
                        .load(menuList.image)
                        .centerCrop()                        //optional
                        .into(holder.menu_img)
                }
            }


        }

        override fun getItemCount(): Int {
            return menuListFilter.size
        }

        inner class Adapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var menu_img: ImageView
            var menu_name: TextView
            var menu_card: CardView
            var diesesCardLyt : RelativeLayout
            init {
                menu_img = itemView.findViewById(R.id.menu_img)
                menu_name = itemView.findViewById(R.id.menu_name)
                menu_card = itemView.findViewById(R.id.menu_card)
                diesesCardLyt = itemView.findViewById(R.id.diesesCardLyt)


                menu_card.setOnClickListener {
                    val position = adapterPosition

                    if (position != RecyclerView.NO_POSITION) {
                        val selectedList: DiesesListDataM =
                            menuListFilter[position]
                        listener.onItemClick(selectedList)

                        val intent = Intent(context, DiesesDetails::class.java)
                        intent.putExtra("list", selectedList)
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