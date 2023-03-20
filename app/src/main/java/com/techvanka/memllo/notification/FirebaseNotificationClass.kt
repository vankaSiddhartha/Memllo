package com.techvanka.memllo.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techvanka.memllo.MainActivity
import com.techvanka.memllo.R
import kotlin.random.Random

class FirebaseNotificationClass:FirebaseMessagingService() {
    private val channelId = "Memllo"
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this,MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

            var inte = PendingIntent.getActivities(this,0, arrayOf(intent),PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,channelId)
            .setContentTitle("Got new message")
            .setSmallIcon(R.drawable.notifications_foreground)
            .setAutoCancel(true)
            .setContentIntent(inte)
            .build()
        manager.notify(Random.nextInt(),notification)


    }
    private fun createNotificationChannel(manager: NotificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =   NotificationChannel(channelId,"memllochat",NotificationManager.IMPORTANCE_HIGH)
            channel.description ="New Chat"
            channel.enableLights(true)
            manager.createNotificationChannel(channel)
        }
    }
}