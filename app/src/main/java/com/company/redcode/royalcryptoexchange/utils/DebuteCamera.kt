package com.example.admin.camerawork

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.models.ImageObject
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView


class DebuteCamera : AppCompatActivity() {
    private val PICK_IMAGES = 6069
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1001
    //    private CameraView cameraView;
    private lateinit var btn_take_photo: CircleImageView
    private lateinit var profile_image0: CircleImageView
    private lateinit var cancle_tv: TextView
    private lateinit var done_TV: TextView
    var filterVariable = ""
    var imgBitmpas = arrayOfNulls<Bitmap>(1)
    var bioList = ArrayList<Bitmap>()
    private var count = 0
    private val TAG = "DebuteCamera"
    private lateinit var camera_Preview_Frame: FrameLayout
    private var camera: Camera? = null
    private var cameraPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
*/
        //window.requestFeature(Window.FEATURE_ACTION_BAR)
        // supportActionBar!!.hide()
        setContentView(R.layout.debute_camera)
        initilizeViews()

        if (ContextCompat.checkSelfPermission(this@DebuteCamera,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initCamera()
        } else {
            askForCameraPermission()
            Toast.makeText(this, "Please Allow app to Use Camera of your Device. Thanks", Toast.LENGTH_SHORT).show()
        }


    }

    private fun askForCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@DebuteCamera, Manifest.permission.CAMERA)) {
            Snackbar.make(findViewById<View>(android.R.id.content), "Need permission for loading data", Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    View.OnClickListener {
                        //asking all required permissions
                        ActivityCompat.requestPermissions(this@DebuteCamera, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                    }).show()
        } else {
            ActivityCompat.requestPermissions(this@DebuteCamera, arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }

    private fun initCamera() {
        if (checkCameraHardware()) {
            camera = getCameraInstance()
            cameraPreview = CameraPreview(this, camera)
            //            CameraPreview.setCameraDisplayOrientation(CameraActivity.this,camera);
            camera_Preview_Frame.addView(cameraPreview)
            setFocus()

        } else {
            Toast.makeText(applicationContext, "Device not support camera feature", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFocus() {
        val params = camera?.getParameters()
        if (params?.supportedFocusModes!!.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else {
            params?.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }
        camera?.parameters = params
    }

    private fun getCameraInstance(): Camera? {
        var c: Camera? = null
        try {
            c = Camera.open()
        } catch (e: Exception) {
            Log.e("TAG", "getCameraInstance: No Camera Found")
        }

        return c
    }



    private fun checkCameraHardware(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
    fun askPermission(permission: String, requestcode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestcode)
        }
    }

    var myShutterCallback: Camera.ShutterCallback = Camera.ShutterCallback {
    }
    private val pictureCallback = Camera.PictureCallback { data, camera ->
        try {
            val bmp:Bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            /*  val out = ByteArrayOutputStream()
             var bar= bmp.compress(Bitmap.CompressFormat.JPEG, 50, out)
              val decoded:Bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
             var bd=  out.toByteArray()*/

            setimage(bmp)

            //if (decoded != null)
            //  RetrieveFeedTask().execute(decoded)

            camera.startPreview()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            camera.startPreview()

        }
    }

    private fun setimage(bmp: Bitmap) {

        if (count == 1) {
            count = 0
        }

        if (count == 0) {

            Glide.with(applicationContext).load(bmp).into(profile_image0)
            imgBitmpas[0] = bmp

        }
        bioList.add(bmp)
        count

    }

    private fun initilizeViews() {

        btn_take_photo = findViewById(R.id.capture_button)
        profile_image0 = findViewById(R.id.profile_image0)
        cancle_tv = findViewById(R.id.cancle_tv)
        done_TV = findViewById(R.id.next_tv)
        camera_Preview_Frame = findViewById(R.id.camera)

        done_TV.setOnClickListener {
            if (imgBitmpas[0] != null) {

                var obj = ImageObject(bioList,null)
                var  gson = Gson();
                var json = gson.toJson(obj)
                var intent = Intent()
                intent.putExtra("camera intent",json)
                setResult(Activity.RESULT_OK,intent)
                this.finishActivity()
            } else {
                Toast.makeText(this@DebuteCamera, "Please Load atleast 1 image.", Toast.LENGTH_SHORT).show()
            }

        }

        btn_take_photo.setOnClickListener {
            try {
                camera?.takePicture(myShutterCallback, null, pictureCallback)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        cancle_tv.setOnClickListener {
            setResult(RESULT_CANCELED)
            finishActivity()
        }
    }
    fun finishActivity(){
        finish()

    }

}
