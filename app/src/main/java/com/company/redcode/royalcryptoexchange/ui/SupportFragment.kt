package com.company.redcode.royalcryptoexchange.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.ImageObject
import com.example.admin.camerawork.CameraActivity
import com.google.gson.Gson


class SupportFragment : Fragment() {
    private val CAMERA_INTENT = 555
    private val REQUSET_GALLERY_CODE: Int = 44
    private var attach_img_1: ImageView? = null
    private var attach_img_2: ImageView? = null
    private var attach_img_3: ImageView? = null
    private var attach_img_4: ImageView? = null
    private var myImgJson: String? = null

    var btn_add: ImageButton? = null
    var coin: String = " "
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.support_fragmnt, container, false)
        initView(view)
        return view

    }

    private fun initView(view: View) {
        btn_add = view!!.findViewById(R.id.btn_add)
        val coin_type_spinner = view.findViewById(R.id.spinner) as Spinner
        val spinnerAdapter = ArrayAdapter.createFromResource(activity!!,
                R.array.array_support_title, android.R.layout.simple_spinner_item)

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        coin_type_spinner.adapter = spinnerAdapter
        coin_type_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val item = parent!!.getItemAtPosition(pos);
                coin = item.toString()
            }
        })
        btn_add!!.setOnClickListener {
            supportImageDialoge()
        }
        attach_img_1 = view!!.findViewById(R.id.attach_img_1)
        attach_img_2 = view!!.findViewById(R.id.attach_img_2)
        attach_img_3 = view!!.findViewById(R.id.attach_img_3)
        attach_img_4 = view!!.findViewById(R.id.attach_img_4)

    }

    private fun supportImageDialoge() {
        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.support_select_image_dialogue, null)
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUSET_GALLERY_CODE && resultCode == Activity.RESULT_OK && data != null) {
            var galList = ArrayList<String>()
            if (data.clipData != null) {
                var count: Int = data.clipData.itemCount
                if (count > 5) {
                    count = 4
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
                    if (i ==4)
                        attach_img_4!!.setImageBitmap(bitmap)

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
                if (i ==3)
                    attach_img_4!!.setImageBitmap(list[i])
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}