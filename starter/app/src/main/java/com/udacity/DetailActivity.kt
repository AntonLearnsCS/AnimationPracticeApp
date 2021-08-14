package com.udacity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.getStringExtra("fileName")
        val fileStatus = intent.getStringExtra("fileStatus")

        val mBinding = ActivityDetailBinding.inflate(layoutInflater)

        mBinding.fileNameEntry.setText(fileName.toString())
        mBinding.statusEntry.setText(fileStatus.toString())

        Log.i("detailName",fileName.toString())
        Log.i("detailStatus",fileStatus.toString())

        /*textView.setText(fileName.toString())
        textView2.setText(fileStatus.toString())*/
      //Allows the ActionBar to condense when we scroll
   // coordinateMotion()



    }

    private fun coordinateMotion() {
        val appBarLayout: AppBarLayout = findViewById(R.id.appBarLayout)
        val motionLayout: MotionLayout = findViewById(R.id.motionLayout)

        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            motionLayout.progress = seekPosition
        }
        appBarLayout.addOnOffsetChangedListener(listener)
    }
}
