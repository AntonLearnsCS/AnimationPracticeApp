package com.udacity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

      //Allows the ActionBar to condense when we scroll
    coordinateMotion()

        val test = intent.getStringExtra("status")
        if (test != null) {
            Log.i("Detail",test)
        }
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
