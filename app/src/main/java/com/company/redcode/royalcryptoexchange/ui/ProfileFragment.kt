package com.company.redcode.royalcryptoexchange.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.adapter.UserBankAdapater
import com.company.redcode.royalcryptoexchange.models.Bank
import com.company.redcode.royalcryptoexchange.models.ImageObject
import com.example.admin.camerawork.CameraActivity
import com.google.gson.Gson



class ProfileFragment : Fragment() {

    var btn_add_bank: Button? = null
    var btn_add_img: Button? = null
    var spinner_payment_method: Spinner? = null
    var bank_recycler_view: RecyclerView? = null
    val SELECT_PICTURE: Int = 4
    private val CAMERA_INTENT = 555
    private val REQUSET_GALLERY_CODE: Int = 44
    private var myImgJson: String? = null
    private var bank_view: LinearLayout? = null
    private var mobile_method_view: LinearLayout? = null
    var adapter: UserBankAdapater? =null

    private var attach_img_1: ImageView? = null
    private var attach_img_2: ImageView? = null
    private var attach_img_3: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View?) {

        btn_add_bank = view!!.findViewById(R.id.addaccount)
        attach_img_1 = view!!.findViewById(R.id.attach_img_1)
        attach_img_2 = view!!.findViewById(R.id.attach_img_2)
        attach_img_3 = view!!.findViewById(R.id.attach_img_3)

        btn_add_img = view!!.findViewById(R.id.btn_add_img)



        btn_add_bank!!.setOnClickListener {
            showTradeDialog()
        }
        btn_add_img!!.setOnClickListener {

            showImageAddDialog()
        }


    }
    private fun showImageAddDialog() {
        val view: View = LayoutInflater.from((activity as Context?)!!).inflate(R.layout.select_image_dialog, null)
        val alertBox = AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        alertBox.setCancelable(true)
        val dialog = alertBox.create()

        val gallery_dialog: ImageView = view.findViewById(R.id.gallery_dialog)
        val camera_dialog: ImageView = view.findViewById(R.id.camera_dialog)

        gallery_dialog.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUSET_GALLERY_CODE)
            dialog.dismiss()
        }
        camera_dialog.setOnClickListener {
            var intent = Intent(activity!!, CameraActivity::class.java)
            startActivityForResult(intent, CAMERA_INTENT)
            dialog.dismiss()
        }


        dialog.show()
    }
    private fun showTradeDialog() {

        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.dialog_new_bank, null)
        val alertBox = android.support.v7.app.AlertDialog.Builder(activity!!)
        alertBox.setView(view)
        val dialog = alertBox.create()

        //-------------------------------------------------------------------------
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        val btnClose: ImageView = view.findViewById(R.id.btn_close)
        bank_recycler_view = view!!.findViewById(R.id.bank_recycler_view)
        bank_view = view!!.findViewById(R.id.bank_view)
        mobile_method_view = view!!.findViewById(R.id.mobile_method_view)
        spinner_payment_method = view!!.findViewById(R.id.spinner_payment_method)

        spinner_payment_method!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val item = parent!!.getItemAtPosition(pos);
                if(item.equals("Bank Transfer")){
                    mobile_method_view!!.visibility = View.GONE
                    bank_view!!.visibility = View.VISIBLE
                }else {
                    mobile_method_view!!.visibility = View.VISIBLE
                    bank_view!!.visibility = View.GONE
                }
            }
        })

         var list = ArrayList<Bank>()
         list.add(Bank("ahmed", "Bank Transfer"))
         list.add(Bank("ahmed", "Bank Transfer"))
         list.add(Bank("ahmed", "Bank Transfer"))
         list.add(Bank("ahmed", "Bank Transfer"))


          adapter = UserBankAdapater(activity!!, list) { position ->
//             showTradeDialog()
             list.removeAt(position)
              adapter!!.notifyDataSetChanged()
         }
         var layout = LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)
         bank_recycler_view!!.layoutManager = layout
         bank_recycler_view!!.adapter = adapter


        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUSET_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {
            var galList = ArrayList<String>()
            if (data.clipData != null) {
                var count: Int = data.clipData.itemCount
                if (count > 4) {
                    count = 3
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imageUri)
                    if (i ==0)
                    attach_img_1!!.setImageBitmap(bitmap)
                    if (i ==1)
                     attach_img_2!!.setImageBitmap(bitmap)
                    if (i ==2)
                     attach_img_3!!.setImageBitmap(bitmap)

                    galList.add(imageUri.toString())
                }

            } else if (data.data != null) {
                val imagePath = data.data
                galList.add(imagePath.toString())
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imagePath)
                attach_img_1!!.setImageBitmap(bitmap)

            }

            var obj = ImageObject(null, galList)
            var gson = Gson();
            myImgJson = gson.toJson(obj)




        } else if (requestCode == CAMERA_INTENT && resultCode == Activity.RESULT_OK && data != null) {
            var result = data!!.getStringExtra("camera intent")
            myImgJson = result
            var obj = Gson().fromJson(myImgJson, ImageObject::class.java)
            var list = obj.camList

            for(i in 0 until list!!.size){

                if (i ==0)
                    attach_img_1!!.setImageBitmap(list[i])
                if (i ==1)
                    attach_img_2!!.setImageBitmap(list[i])
                if (i ==2)
                    attach_img_3!!.setImageBitmap(list[i])
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
