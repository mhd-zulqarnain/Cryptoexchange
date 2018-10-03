package com.company.redcode.royalcryptoexchange.service


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat

import com.company.redcode.royalcryptoexchange.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject
import android.annotation.SuppressLint
import com.company.redcode.royalcryptoexchange.DrawerActivity
import android.app.NotificationChannel
import android.os.Build
import com.company.redcode.royalcryptoexchange.OrderDetailActivity
import com.google.gson.Gson


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var type = ""
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage!!.data.size > 0) {
            type = "json"
        }
        if (remoteMessage.notification != null) {
           var  messege = remoteMessage.notification.body
//            sendNotification(remoteMessage.notification.body!!)

            var data =  remoteMessage.notification.title!!.split(",")
            val intent = Intent(this@MyFirebaseMessagingService, OrderDetailActivity::class.java)

            intent.putExtra("type", "service")
            intent.putExtra("orderId", data[1])
            intent.putExtra("request", data[0])


//        val intent = Intent(this, DrawerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = "Default"
            val builder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher_logo)
                    .setContentTitle("RoyalCrypto")
                    .setContentText( messege).setAutoCancel(true).setContentIntent(pendingIntent)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            manager.notify(0, builder.build())

        }
    }

 /*   private fun sendNotification(messegeBody: String) {
        var id = ""
        var messege = ""
        var type =messegeBody.notification.body
       *//* if (type.equals("json")) {
            try {
                val jsonObject = JSONObject(messegeBody)
                id = jsonObject.getString("title")
                messege = jsonObject.getString("body")
                title = jsonObject.getString("intent")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else if (type.equals("messege")) {
            messege = messegeBody
        }*//*

     *//*   val intent = Intent(this@MyFirebaseMessagingService, DrawerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 *//**//* Request code *//**//*, intent,
                PendingIntent.FLAG_ONE_SHOT)*//*

     *//*   val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this@MyFirebaseMessagingService)
                .setContentTitle(title )
                .setSmallIcon(R.mipmap.ic_launcher_logo)
                .setContentText(messege)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
*//*



    }*/
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