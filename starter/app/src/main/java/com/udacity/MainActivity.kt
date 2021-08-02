package com.udacity

import android.animation.ObjectAnimator
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
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.udacity.databinding.ContentMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.InternalCoroutinesApi

private val NOTIFICATION_ID = 0

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    //for actions like snooze
    private lateinit var action: NotificationCompat.Action
    private lateinit var viewModel : ViewModel

    /*private var i = 0
    private val handler = Handler(Looper.getMainLooper()).postDelayed({
        // Your Code
        progressBar!!.progress = i
    }, 3000)

    *//*    val binding: ContentMainBinding = DataBindingUtil.inflate(
        layoutInflater, R.layout.content_main, C, false)*//*


    private var progressBar: ProgressBar? = null*/


    @InternalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {

                if (viewModel.currentUrl.value != null)
                {
                    download()
                    Toast.makeText(applicationContext, "Select downloadable item", Toast.LENGTH_SHORT).show()
                    //set state
                    custom_button.setMyButtonState(ButtonState.Clicked)
                    custom_button.setMyButtonState(ButtonState.Loading)
                }
                else
                {
                    Toast.makeText(applicationContext, "Select downloadable item", Toast.LENGTH_SHORT).show()

                }
        }
         notificationManager = this.getSystemService(
            NotificationManager::class.java
        ) as NotificationManager

    createChannel(
        //since the channel and send notification uses the same channel_id, the notification will be passed through said channel
        getString(R.string.notification_id),
        getString(R.string.notification_name))

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)
    }



    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.appLoad ->
                    if (checked) {
                        viewModel.setUrl(AppUrl)
                    }
                R.id.glide ->
                    if (checked) {
                        viewModel.setUrl(GlideUrl)
                    }
                R.id.retrofit ->
                    if (checked)
                    {
                        viewModel.setUrl(RetrofitUrl)
                    }
            }
        }
    }

    //Context-registered receivers
    @InternalCoroutinesApi
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
            custom_button.setMyButtonState(ButtonState.Completed)
        }
    }
    //when to call this function, in a broadcast class?

        @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )

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
            DownloadManager.Request(Uri.parse(viewModel.currentUrl.value))
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
        private const val AppUrl =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val GlideUrl = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"

        private const val RetrofitUrl = "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        private const val CHANNEL_ID = "channelId"
    }
    //Note: Each scope function adds context to the one that already exists (context of our class or outer function).
//So since we want to use this extension function outside of our MainActivity class, we define the extension function inside of
//said class

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

        //note that the Intent is created inside the function (as opposed to BroadcastReceiver class)
        val detailIntent = Intent(applicationContext, DetailActivity::class.java)

        pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_id)
        )
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext
                .getString(R.string.notification_title)) //R
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setContentText(messageBody)
            .build()

        notify(NOTIFICATION_ID, builder)
    }
    fun horizontalLoadingScreen ()
    {
       // val animator = ObjectAnimator.ofArgb(custom_button,"backgroundColor", Color.)


    }

    fun NotificationManager.cancelNotifications() {
        cancelAll()
    }
}
