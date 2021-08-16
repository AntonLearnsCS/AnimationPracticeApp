package com.udacity

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


private lateinit var pendingIntent: PendingIntent
//for actions like snooze
private lateinit var action: NotificationCompat.Action
private val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, fileName : String, fileStatus : String) {

    //note that the Intent is created inside the function (as opposed to BroadcastReceiver class)
    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtra("fileName",fileName)
    detailIntent.putExtra("fileStatus",fileStatus)

    pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    action = NotificationCompat.Action(null,"check",pendingIntent)


    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title)) //R
        .setContentIntent(pendingIntent)
        .addAction(action)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentText(messageBody)
        .build()

    notify(NOTIFICATION_ID, builder)
}