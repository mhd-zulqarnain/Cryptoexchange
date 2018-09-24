package com.company.redcode.royalcryptoexchange.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.ImageObject
import com.example.admin.camerawork.CameraActivity
import com.example.admin.camerawork.DebuteCamera
import com.google.gson.Gson


class DisputetFragment : Fragment() {
    private val CAMERA_INTENT = 555
    private val REQUSET_GALLERY_CODE: Int = 44
    private var attach_img_1: ImageView? = null
    private var myImgJson: String? = null
    var image_view: ImageView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_disputet, container, false)
        initView(view)
        return view

    }

    private fun initView(view: View) {
         image_view = view!!.findViewById(R.id.image_view)

         image_view!!.setOnClickListener {
            supportImageDialoge()
        }
        attach_img_1 = view!!.findViewById(R.id.image_view)

    }

    private fun supportImageDialoge() {
        val view: View = LayoutInflater.from(activity!!).inflate(R.layout.select_image_dialog, null)
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
            var intent = Intent(activity!!, DebuteCamera::class.java)
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
                if (count > 1) {
                    count = 0
                }
                for (i in 0 until count) {
                    val imageUri = data.clipData.getItemAt(i).uri
                    val bitmap = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), imageUri)
                    if (i ==0)
                        attach_img_1!!.setImageBitmap(bitmap)

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

            for(i in 0 until list!!.size) {

                if (i == 0)
                    attach_img_1!!.setImageBitmap(list[i])
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}


