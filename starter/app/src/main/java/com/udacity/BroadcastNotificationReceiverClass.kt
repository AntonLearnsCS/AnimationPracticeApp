package com.udacity

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService

//may not be needed
/*
class BroadcastNotificationReceiverClass : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        */
/*val notificationManager = context?.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

         *//*

        val notificationManager = context?.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        notificationManager.sendNotification(
            context.getText(R.string.app_description).toString(),
            context
        )
    }
}*/
