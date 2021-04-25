package com.doctortree.doctortree.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doctortree.doctortree.R
import com.doctortree.doctortree.data.DiesesListDataM
import com.doctortree.doctortree.data.MessageListDataM
import com.doctortree.doctortree.request.MessageListRequestM
import com.doctortree.doctortree.util.FileSizeRestriction
import com.doctortree.doctortree.util.GlobalVeriable
import com.doctortree.doctortree.viewModel.MessageListViewM
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.ContentUriUtils
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.net.URISyntaxException

class Messaging : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val RC_PHOTO_PICKER_PERM = 123
        const val RC_FILE_PICKER_PERM = 321
    }

    private val MAX_ATTACHMENT_COUNT = 1
    private var photoPaths = java.util.ArrayList<Uri>()
    private var docPaths = java.util.ArrayList<Uri>()

    private var filePaths =
        java.util.ArrayList<Uri>()

    var files = arrayOfNulls<Uri>(6)//emptyArray<Uri>()

    private lateinit var messageListViewM : MessageListViewM
    private lateinit var messageList : ArrayList<MessageListDataM>

    private lateinit var globalVeriable: GlobalVeriable


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    //*********** Toolbar ************//
    private lateinit var toolbar: Toolbar

    //********** Buttons *****************//
    private lateinit var btnMessageImage : ImageView

    var choose:Int = 0
    var pDocNid:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        initialization()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "জরুরি জিজ্ঞাসা"

        btnMessageImage.setOnClickListener{
            chooseFileType("Choose Photo")
            choose = pDocNid
        }

        getMessage()
       // observeMessage()
        observeViewModel()
    }


    //*************** File Chooser ********************//

    private fun chooseFileType(title: String) {
        val dialog = Dialog(CreditCardRequestActivity@this)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.choose_upload_type_view)
        dialog.setTitle("Choose Document Type..")
        var buttonCancle: Button
        var dTitle:TextView
        dTitle = dialog.findViewById(R.id.dialogTitle)
        dTitle.text = title
        buttonCancle = dialog.findViewById(R.id.btnCancel)
        val pic = dialog.findViewById(R.id.lo_pic) as LinearLayout
        pic.setOnClickListener {
            pickPhotoClicked()
            dialog.dismiss()
        }
        buttonCancle.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    @AfterPermissionGranted(Messaging.RC_PHOTO_PICKER_PERM)
    fun pickPhotoClicked() {
        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER)) {
            onPickPhoto()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_photo_picker),
                Messaging.RC_PHOTO_PICKER_PERM,
                FilePickerConst.PERMISSIONS_FILE_PICKER
            )
        }
    }

    fun onPickPhoto() {
        /*val maxCount: Int = MAX_ATTACHMENT_COUNT - docPaths.size
        if (docPaths.size + photoPaths.size == MAX_ATTACHMENT_COUNT) {
            Toast.makeText(
                this, "Cannot select more than $MAX_ATTACHMENT_COUNT items",
                Toast.LENGTH_SHORT
            ).show()
        } else {*/
        FilePickerBuilder.instance
            //.setMaxCount(maxCount)
            .setMaxCount(1)
            .setSelectedFiles(photoPaths) //this is optional
            .setActivityTheme(R.style.FilePickerTheme)
            .setActivityTitle("Please select media")
            .enableVideoPicker(true)
            .enableCameraSupport(true)
            .showGifs(true)
            .showFolderView(true)
            .enableSelectAll(false)
            .enableImagePicker(true)
            .setCameraPlaceholder(R.drawable.custom_camera)
            .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .pickPhoto(this, FilePickerConst.REQUEST_CODE_PHOTO)
        // }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult", "Action in OnActivity- Request Code: $requestCode")
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                val dataList =
                    data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                if (dataList != null) {
                    photoPaths = java.util.ArrayList()
                    photoPaths.addAll(dataList)
                    try {
                        //singleFileUpload(dataList.get(0));
                        //multipleFileUpload(dataList)
                        var res: Boolean =
                            FileSizeRestriction.isValidFileSize("" + ContentUriUtils.getFilePath(
                                this,
                                dataList[0]))
                        if (res) {
                            files[choose] = dataList[0]
                            //setImage(dataList[0])
                            Log.e("onActivityResult", "" + files[choose])
                        } else {
                            FileSizeRestriction.showDocSizeError(this)
                        }
                    } catch (e: Exception) {
                        Log.e("onActivityResult", "" + e.toString())
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        //addThemToView(photoPaths, docPaths)
    }

    //*************** File Chooser ********************//


    private fun getMessage() {
        val model = MessageListRequestM()
        model.receiver_id = "1"
        model.sender_id = ""+globalVeriable.id

        this.let { it1 -> messageListViewM.getMessageList(model,it1) }
    }

    fun observeViewModel() {

        messageListViewM.messageList.observe(
            this,
            androidx.lifecycle.Observer {

                it?.let {

                    if (null != it) {
                        messageList.clear()
                        messageList = ArrayList<MessageListDataM>()
                        for (i in 0 until it.size) {

                            val model = MessageListDataM(
                                it.get(i).created_at,
                                it.get(i).error,
                                it.get(i).id,
                                it.get(i).image,
                                it.get(i).message,
                                it.get(i).receiver_id.toString() + "",
                                it.get(i).receiver_name.toString() + "",
                                it.get(i).sender_id.toString() + "",
                                it.get(i).sender_name.toString() + ""
                            )

                            messageList.add(model)

                        }
                        Log.e("size-->", messageList.size.toString())
                        /*
                        val linearLayoutManager = LinearLayoutManager(this)
                        recyclerView.layoutManager = linearLayoutManager
                        recyclerView.setHasFixedSize(true)
                        recyclerView.adapter = AgentOnBoardingRequestListAdapter(requestList, this@AgentOnBoardingRequestStatusV)
                        */

                        //pDialog.dismiss()

                        viewManager = LinearLayoutManager(this)
                        viewAdapter =
                            MessageListAdapter(
                                messageList,
                                this,
                                object :
                                    MessageListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: MessageListDataM?) {

                                    }
                                })

                        recyclerView = findViewById<RecyclerView>(R.id.rvMessage).apply {
                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            setHasFixedSize(true)

                            viewManager.isAutoMeasureEnabled = false

                            // use a linear layout manager
                            layoutManager = viewManager

                            // specify an viewAdapter (see also next example)
                            adapter = viewAdapter



                        }


                    } else {
                        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }

    private fun observeMessage() {
        messageListViewM.messageList.observe(
            this,{

                it?.let {
                    try {
                        messageList.clear()
                    }catch (e:Exception){

                    }

                    messageList = ArrayList<MessageListDataM>()

                    for (i in 0 until it.size) {
                        val model = MessageListDataM(
                            it.get(i).created_at.toString(),
                            it.get(i).error,
                            it.get(i).id,
                            it.get(i).image,
                            it.get(i).message,
                            it.get(i).receiver_id,
                            it.get(i).receiver_name,
                            it.get(i).sender_id,
                            it.get(i).sender_name
                        )

                        messageList.add(model)
                        Log.e("MSize", messageList.size.toString())

                    }

                        viewManager = LinearLayoutManager(this)
                        viewAdapter =
                            MessageListAdapter(
                                messageList,
                                this,
                                object :
                                    MessageListAdapter.OnItemClickListener {
                                    override fun onItemClick(item: MessageListDataM?) {

                                    }
                                })

                        recyclerView = findViewById<RecyclerView>(R.id.rvMessage).apply {
                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            setHasFixedSize(true)

                            viewManager.isAutoMeasureEnabled = false;

                            // use a linear layout manager
                            layoutManager = GridLayoutManager(this@Messaging,1)

                            //recyclerView.layoutManager = GridLayoutManager(this@Dieses, 2)

                            // specify an viewAdapter (see also next example)
                            adapter = viewAdapter

                        }

                    }


            })
    }



    private fun initialization() {

        messageListViewM = ViewModelProvider(this).get(MessageListViewM::class.java)
        messageList = ArrayList<MessageListDataM>()

        globalVeriable = this.applicationContext as GlobalVeriable

        btnMessageImage = findViewById(R.id.btnMessageImage)

    }

    //***************** Adapter Class start here *********************//

    class MessageListAdapter(
        private val messageList: ArrayList<MessageListDataM>,
        private val context: Context,
        listenerInit: OnItemClickListener,

        ) : RecyclerView.Adapter<MessageListAdapter.Adapter>() {


        var messageListFilter = ArrayList<MessageListDataM>()
        var listener: OnItemClickListener
        var globalVeriable : GlobalVeriable

        init {
            messageListFilter = messageList
            listener = listenerInit
            globalVeriable = context.applicationContext as GlobalVeriable
        }

        interface OnItemClickListener {
            fun onItemClick(item: MessageListDataM?)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): Adapter {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_card, parent, false)
            return Adapter(view)
        }

        override fun onBindViewHolder(
            holder: Adapter,
            position: Int,
        ) {
            val messageList = messageListFilter[position]

//            if (messageList.image==null){
//                holder.image_card.visibility = View.GONE
//            }else if ( messageList.image!!.isEmpty()){
//                holder.image_card.visibility = View.GONE
//            }else{
//                holder.image_card.visibility = View.VISIBLE
//                Glide.with(context)
//                    .load(messageList.image)
//                    .into(holder.message_img)
//            }

            if (messageList.message == null && messageList.image == null){
                holder.message_card.visibility = View.GONE
                holder.image_card.visibility = View.GONE
            }else if (messageList.message == null){
                holder.message_card.visibility = View.GONE
                holder.image_card.visibility = View.VISIBLE
                Glide.with(context)
                    .load(messageList.image)
                    .into(holder.message_img)
            }else if (messageList.image == null){
                holder.message_card.visibility = View.VISIBLE
                holder.message.text = messageList.message
                holder.image_card.visibility = View.GONE
            }

            else{
                holder.message_card.visibility = View.VISIBLE
                holder.message.visibility = View.VISIBLE
                holder.message.text = messageList.message
                Glide.with(context)
                    .load(messageList.image)
                    .into(holder.message_img)
            }



        }

        override fun getItemCount(): Int {
            return messageListFilter.size
        }

        inner class Adapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var message_img: ImageView
            var message: TextView
            var image_card: CardView
            var message_card : CardView
            init {
                message_img = itemView.findViewById(R.id.imgMessage)
                message = itemView.findViewById(R.id.tvMessage)
                image_card = itemView.findViewById(R.id.imageCard)
                message_card = itemView.findViewById(R.id.messageCard)


                message_card.setOnClickListener {
                    val position = adapterPosition

                    if (position != RecyclerView.NO_POSITION) {
                        val selectedList: MessageListDataM =
                            messageListFilter[position]
                        listener.onItemClick(selectedList)

                        //Toast.makeText(context, message_img, Toast.LENGTH_SHORT).show()

//                        val intent = Intent(context, DiesesDetails::class.java)
//                        intent.putExtra("list", selectedList)
//                        context.startActivity(intent)

                    } else {
                        Toast.makeText(context, "No Position", Toast.LENGTH_SHORT).show()
                    }
                }


            }

        }


    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    //***************** Adapter Class end here *********************//
}