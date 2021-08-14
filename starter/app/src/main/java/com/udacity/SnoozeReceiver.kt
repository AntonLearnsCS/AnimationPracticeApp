package com.udacity

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.text.format.DateUtils
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat

class SnoozeReceiver: BroadcastReceiver() {
    private val REQUEST_CODE = 0

    override fun onReceive(context: Context, intent: Intent) {
        val triggerTime = SystemClock.elapsedRealtime() + DateUtils.MINUTE_IN_MILLIS

        val notifyIntent = Intent(context, receiver::class.java)
        //"PendingIntent.getBroadcast" retrieves a PendingIntent that will perform a broadcast, like calling Context.sendBroadcast().
        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //"setExactAndAllowWhileIdle" schedules an alarm to be delivered precisely at the stated time.
        //In this case AlarmManager will initiate the AlarmReceiver class via "notifyPendingIntent"
        //AlarmManagerCompat is a system wide manager so we need to wrap our intent with pendingIntent for the alarm manager
        //to recognize and use the intent
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.ELAPSED_REALTIME_WAKEUP, //meaning will wake up device if device is currently asleep
            triggerTime, //the time at which the alarm will go off
            notifyPendingIntent
        )

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()

    }

}