package com.udacity

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    //new code
    private var progress = 0f
    private var progressAngle = 0f
    private var buttonText = ""

    private var buttonBackgroundColor = 0

    private var circleColor = 0

    private var buttonAnimator = ObjectAnimator()
    private var circleAnimator = ObjectAnimator()



    private val paintButton = Paint()
    private val paintCircle = Paint()

    // KProperty<*>, ButtonState, ButtonState  "kproperty" - Represents a property, such as a named val or var declaration.
    // Instances of this class are obtainable by the :: operator.
    //.observable means that the ButtonState instances are able to be observed
    //it seems that every Delegate will have the "p", "old" and "new" variables to be manipulated

    /*
    A delegate is just a class that provides the value for a property and handles its changes.
    This allows us to move, or delegate, the getter-setter logic from the property itself to a separate class,
    letting us reuse this logic.
    source: https://proandroiddev.com/kotlin-delegates-in-android-1ab0a715762d
     */
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = " LOADING..."
                // button animation set up //.ofFloat(0f, measuredWidth.toFloat())
                //buttonAnimator = ObjectAnimator()
                    buttonAnimator.apply {
                        setObjectValues(0F,3600F)
                        setDuration(2000)
                        repeatMode = ObjectAnimator.RESTART
                        repeatCount = ObjectAnimator.INFINITE
                        addUpdateListener {
                            progress = animatedValue as Float
                            this@LoadingButton.invalidate()
                        }
                        start()
                    }
                // circle animation set up
                circleAnimator.apply {
                    setObjectValues(0F,3600F)
                    duration = 2000
                    repeatMode = ObjectAnimator.RESTART
                    repeatCount = ObjectAnimator.INFINITE

                    //interpolator = AccelerateInterpolator(1f)
                    addUpdateListener {
                        progressAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }
            }


            ButtonState.Completed -> {
                buttonText = "FINISHED"

                //ends animation
                buttonAnimator.end()
                circleAnimator.end()
            }
            else -> {
                ButtonState.Clicked
                buttonText = "DOWNLOAD"
            }
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        if (buttonState == ButtonState.Completed)
        {
            buttonAnimator.isPaused
            circleAnimator.isPaused
        }
        when (buttonState) {
            buttonState -> ButtonState.Clicked
            buttonState -> ButtonState.Loading
            else -> ButtonState.Completed
        }
        //redraw
        if (buttonState != ButtonState.Completed)
        invalidate()

        return true
    }

    init {
        //isClickable = true
        buttonText = "DOWNLOAD"
        buttonState = ButtonState.Clicked

        //Use  https://developer.android.com/reference/kotlin/android/util/AttributeSet

        context.theme.obtainStyledAttributes(attrs, R.styleable.customButton, 0, 0).apply {
            buttonBackgroundColor = getColor(R.styleable.customButton_buttonColor, 0)
            circleColor = getColor(R.styleable.customButton_circleColor,0)
        }

        paintButton.color = buttonBackgroundColor
        paintCircle.color = circleColor
    }

    override fun onDraw(canvas: Canvas?) {
        paintButton.color = buttonBackgroundColor

        super.onDraw(canvas)
        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintButton)

        paintButton.color = context.getColor(R.color.colorPrimaryDark)
        canvas.drawRect(0f, 0f, progress, measuredHeight.toFloat(), paintButton)

        //change color for text
        paintButton.apply {
            color = context.getColor(R.color.black)
            textAlign = Paint.Align.CENTER
            textSize = 60f
            typeface = Typeface.create("", Typeface.BOLD)
        }

        canvas.drawText(
            buttonText,
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            paintButton
        )

        //Q: Can I draw a filled progress circle with canvas.drawCircle?
        canvas.drawArc(//left, top, right, bottom, start angle, sweep angle, use center, Paint
            measuredWidth - 100f,
            (measuredHeight / 2) - 50f,
            measuredWidth - 50f,
            (measuredHeight / 2) + 50f,
            180f, progressAngle, true, paintCircle
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        setMeasuredDimension(w, h)
    }

    fun setState(state: ButtonState) {
        buttonState = state
    }
}