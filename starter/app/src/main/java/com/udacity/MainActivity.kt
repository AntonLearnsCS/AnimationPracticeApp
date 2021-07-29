package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private val NOTIFICATION_ID = 0

class MainActivity : AppCompatActivity() {
    init {
     /*   val notificationManager = this.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        notificationManager.sendNotification(
            this.getText(R.string.app_description).toString(),
            this
        )*/
    }

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
         notificationManager = this.getSystemService(
            NotificationManager::class.java
        ) as NotificationManager

    createChannel(
        //since the channel and send notification uses the same channel_id, the notification will be passed through said channel
        getString(R.string.notification_id),
        getString(R.string.notification_name))
    }
    //Context-registered receivers
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //"EXTRA_DOWNLOAD_ID" - Intent extra included with ACTION_DOWNLOAD_COMPLETE intents, indicating the ID
            // (as a long) of the download that just completed.
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (context != null) {
                notificationManager.sendNotification(
                    context.getText(R.string.app_description).toString(),
                    context
                )
            }
        }
    }
    //when to call this function, in a broadcast class?

        @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String, channelName: String) {

            // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_LOW
            )
            // TODO: Step 2.6 disable badges for this channel

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
//The download manager is a system service that handles long-running HTTP downloads.
//Clients may request that a URI be downloaded to a particular destination file.
    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        //an ID for the download, unique across the system. This ID is used to make future calls related to this download.
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}
//Note: Each scope function adds context to the one that already exists (context of our class or outer function).
//So since we want to use this extension function outside of our MainActivity class, we define the extension function outside of
//said class
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    //note that the Intent is created inside the function (as opposed to BroadcastReceiver class)
    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_id)
    )
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setContentText(messageBody)
        .build()

    notify(NOTIFICATION_ID, builder)
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}