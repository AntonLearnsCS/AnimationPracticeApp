package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.blue
import androidx.core.graphics.green
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private val tlbuffer = R.dimen.button_margin
    private val brbuffer = R.dimen.button_bottom_right_margin

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        widthSize = width
        heightSize = height
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //float left, float top, float right, float bottom, Paint paint)

        canvas?.drawRect(tlbuffer.toFloat(), tlbuffer.toFloat(), (brbuffer - tlbuffer).toFloat(), (brbuffer - tlbuffer).toFloat()
            , paint)
        canvas?.drawText("Button",(widthSize/2).toFloat(),(heightSize/2).toFloat(), paint)
    }
    //Measure the view and its content to determine the measured width and the measured height.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}