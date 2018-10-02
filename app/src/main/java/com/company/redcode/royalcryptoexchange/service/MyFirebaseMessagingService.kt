package com.company.redcode.royalcryptoexchange.service


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.company.redcode.royalcryptoexchange.R
import com.company.redcode.royalcryptoexchange.auth.SignInActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.os.Vibrator
import android.graphics.BitmapFactory
import org.json.JSONException
import org.json.JSONObject
import android.annotation.SuppressLint
import com.company.redcode.royalcryptoexchange.DrawerActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var type = ""
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage!!.data.size > 0) {
            type = "json"
            sendNotification(remoteMessage.data.toString())
        }
        if (remoteMessage.notification != null) {
            type = "messege"
            sendNotification(remoteMessage.notification.body!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(messegeBody: String) {
        var id = ""
        var messege = ""
        var title = ""
        if (type.equals("json")) {
            try {
                val jsonObject = JSONObject(messegeBody)
                id = jsonObject.getString("id")
                messege = jsonObject.getString("messege")
                title = jsonObject.getString("title")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else if (type.equals("messege")) {
            messege = messegeBody
        }

        val intent = Intent(this@MyFirebaseMessagingService, DrawerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this@MyFirebaseMessagingService)
                .setContentTitle(title )
                .setSmallIcon(R.mipmap.ic_launcher_logo)
                .setContentText(messege)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

    }
   /* private fun showNotification(title: String, vacancy: String) {
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle("New job of $title has been added ")
                .setSmallIcon(R.mipmap.ic_launcher_logo)
                .setContentText("Vanacy $vacancy")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build())
    }

*/

}