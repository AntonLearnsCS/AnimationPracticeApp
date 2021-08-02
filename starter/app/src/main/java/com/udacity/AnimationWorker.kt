
package com.udacity

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.WorkerThread
/*
//TODO: Create Worker, what class do I extend to be able
class AnimationWorker() : Thread("Custom Thread") {
    lateinit var mHandler: Handler
    override fun run() {
        Looper.prepare()
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                //Log.d(TAG, "Looper name " + looper.thread.name)
                looper.thread.interrupt()
            }
        }
        Looper.loop()
    }
}
*/
