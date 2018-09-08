package com.example.admin.camerawork

import android.app.Activity
import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import java.io.IOException



class CameraPreview(var ctx: Context, var camera: Camera?) : SurfaceView(ctx), SurfaceHolder.Callback {




    private lateinit var mSurfaceHolder: SurfaceHolder

    init {
        this.camera = camera
        mSurfaceHolder = holder
        mSurfaceHolder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (camera == null) {
            return
        }
        try {
            camera?.setPreviewDisplay(holder)
            params(camera!!)
            camera?.startPreview()
        } catch (e: IOException) {
            Log.e("tag", "Error setting camera preview: " + e.message)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (holder.surface == null) {
            return
        }

        try {
            camera!!.stopPreview()
        } catch (e: Exception) {
            Log.e("tag", "Error setting camera stop: " + e.message)
        }

        try {
            camera!!.setPreviewDisplay(holder)
            params(camera!!)
            setCameraDisplayOrientation(context as Activity, camera!!)
            camera?.startPreview()
        } catch (e: IOException) {
            Log.e("tag", "Error setting camera preview: " + e.message)
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    private fun params(camera: Camera) {

        val parameters = camera.parameters

        parameters.jpegQuality = 100
        parameters.setRotation(90)
        val display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        if (display.rotation == Surface.ROTATION_0) {
            camera.setDisplayOrientation(90)
        } else if (display.rotation == Surface.ROTATION_270) {
            camera.setDisplayOrientation(180)
        }
        val sizes = parameters.supportedPictureSizes
        var size: Camera.Size = sizes[0]
        for (i in sizes.indices) {
            if (sizes[i].width > size.width)
                size = sizes[i]
        }
        parameters.setPictureSize(size.width, size.height)
        camera.parameters = parameters

    }

    fun setCameraDisplayOrientation(activity: Activity, camera: android.hardware.Camera) {
        val info = android.hardware.Camera.CameraInfo()
        android.hardware.Camera.getCameraInfo(CAMERA_FACING_BACK, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        val result: Int
        result = (info.orientation - degrees + 360) % 360
        camera.setDisplayOrientation(result)
    }
}