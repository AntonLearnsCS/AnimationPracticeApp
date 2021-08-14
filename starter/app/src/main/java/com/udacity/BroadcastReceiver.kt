package com.udacity

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.InternalCoroutinesApi

//Context-registered receivers
@InternalCoroutinesApi
public class BroadcastReceiver(mContext : Context) {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //"EXTRA_DOWNLOAD_ID" - Intent extra included with ACTION_DOWNLOAD_COMPLETE intents, indicating the ID
            // (as a long) of the download that just completed.
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            //from: https://stackoverflow.com/questions/58083140/how-to-download-file-using-downloadmanager-in-api-29-or-android-q
            val downloadManager =
                getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager

            //finds the file downloaded based on the files "id"; returned object is of type "DownloadManager.Query"
            val fileDownloaded = id?.let { DownloadManager.Query().setFilterById(it) }
            //The Cursor interface provides random read-write access to the result set returned by a database query.
            val cursor: Cursor = downloadManager.query(fileDownloaded)


            //Move the cursor to the first row. This method will return false if the cursor is empty.
            if (cursor.moveToFirst()) {
                //getInt - Returns the value of the requested column as an int.
                //getColumnIndex - Returns the zero-based index for the given column name, or -1 if the column doesn't exist.
                //overall, the line below is saying to get the integer value of the column that contains the download status
                //so it looks like each column just has one row since we are always returning the zero-based index
                fileStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }

            when (fileStatus) {
                DownloadManager.STATUS_SUCCESSFUL -> { //"DownloadManager.STATUS_SUCCESSFUL" has a constant value of 8
                    status = "Success"
                }
                DownloadManager.STATUS_FAILED -> {
                    status = "Fail"
                }
            }

            if (context != null) {
                notificationManager.sendNotification(
                    context.getText(R.string.app_description).toString(),
                    context, fileName, status
                )
            }
            custom_button.setState(ButtonState.Completed)
        }
    }
}