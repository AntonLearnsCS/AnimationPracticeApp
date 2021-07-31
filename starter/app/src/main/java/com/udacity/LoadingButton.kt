package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates
import kotlin.time.Duration

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var btnValueAnimator = ValueAnimator()
    var progress = 0F

    private var loadingTextWidth = 0
    private var loadingTextHeight = 0

    private var circleX = 6F
    private var circleY = 6F
    private var circleRadius = 40F

    private val tlbuffer = R.dimen.button_margin
    private val brbuffer = R.dimen.button_bottom_right_margin

    private var circleAnimator = ObjectAnimator()
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
                // will animate draw the rectangle's width based on "progress"
                btnAnimator()


            }

            ButtonState.Completed ->
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
        loadingTextWidth = width
        loadingTextHeight = height
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //float left, float top, float right, float bottom, Paint paint)
        canvas?.drawRect(tlbuffer.toFloat(), tlbuffer.toFloat(), (brbuffer - tlbuffer).toFloat(), (brbuffer - tlbuffer).toFloat()
            , paint)
        paint.color = Color.BLACK
        canvas?.drawText("Button",(loadingTextWidth/2).toFloat(),(loadingTextHeight/2).toFloat(), paint)

        //Q: Can you dynamically fill a circle drawn from Canvas?
        canvas?.drawCircle(loadingTextWidth-circleRadius,loadingTextHeight-circleRadius,circleRadius,paint)
        //TODO: Clip canvas before adding customView

        if (buttonState == ButtonState.Loading)
        {
            paint.color = Color.CYAN
            //will overlay
            canvas?.drawRect(tlbuffer.toFloat(), tlbuffer.toFloat(), progress, loadingTextHeight.toFloat(), paint)
        }


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
        loadingTextWidth = w
        loadingTextHeight = h
        setMeasuredDimension(w, h)
    }
//The call to super.performClick() must happen first, which enables accessibility events as well as calls onClickListener().
    override fun performClick(): Boolean {
        if (super.performClick()) return true

    Toast.makeText(this.context, "Clicked Button", Toast.LENGTH_SHORT).show()

    return true
    }

    fun btnAnimator() {
        var objectAnimator = ObjectAnimator()
        objectAnimator.setObjectValues(0F,2500F).apply {
            objectAnimator.setDuration(2000)
            objectAnimator.repeatCount = Animation.INFINITE
            objectAnimator.repeatMode = ValueAnimator.RESTART
            ValueAnimator.AnimatorUpdateListener{
                valueAnimator ->
                progress = valueAnimator.animatedValue as Float
            }
        }
//copied
       /* btnValueAnimator = ValueAnimator.ofFloat(0F, 2500F).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            ValueAnimator.AnimatorUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                // valueAnimator.interpolator = LinearInterpolator()         // default, so not really needed
                custom_button.invalidate()
            }
            // disable during animation
           // btnValueAnimator.disableDuringAnimation(custom_button)
            start()
        }*/
    }
}