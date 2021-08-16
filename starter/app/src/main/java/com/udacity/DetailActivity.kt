package com.udacity

//import com.udacity.databinding.ActivityDetailBinding
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.getStringExtra("fileName")
        val fileStatus = intent.getStringExtra("fileStatus")

        /*val mBinding = ActivityDetailBinding.inflate(layoutInflater)

        mBinding.fileNameEntry.setText(fileName.toString())
        mBinding.statusEntry.setText(fileStatus.toString())*/

        //Interestingly, when we use layout tag we cannot use setText or .text to update the text in the textView
        fileNameEntry.setText(fileName.toString())
        statusEntry.setText(fileStatus.toString())


        /*val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)*/
        FAB.setOnClickListener {
            val returnIntent = Intent(this, MainActivity::class.java)
            startActivity(returnIntent)
        }


    }

}
