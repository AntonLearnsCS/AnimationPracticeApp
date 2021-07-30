package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
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

    // KProperty<*>, ButtonState, ButtonState  "kproperty" - Represents a property, such as a named val or var declaration.
    // Instances of this class are obtainable by the :: operator.
    //.observable means that the ButtonState instances are able to be observed
    //it seems that every Delegate will have the "p", "old" and "new" variables to be manipulated
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) {p, old, new ->
        //we call "new" b/c it is the most recent value passed in
        when(new)
        {
            ButtonState.Loading ->
            {

            }

        }
    }

    fun setMyButtonState(state : ButtonState)
    {
        buttonState = state
    }

    init {
        isClickable = true
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color = Color.WHITE
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
        paint.color = Color.BLACK
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
//The call to super.performClick() must happen first, which enables accessibility events as well as calls onClickListener().
    override fun performClick(): Boolean {
        if (super.performClick()) return true

    Toast.makeText(this.context, "Clicked Button", Toast.LENGTH_SHORT).show()

    return true
    }
}