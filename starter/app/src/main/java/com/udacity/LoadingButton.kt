package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.res.ResourcesCompat.getColor
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    //new code
    private var progress = 0f
    private var loadingAngle = 0f
    private var buttonText = ""

    private var buttonBackgroundColor = 0

    private var buttonTextColor = 0
    private var circleColor = 0

    private var buttonAnimator = ObjectAnimator()
    private var circleAnimator = ObjectAnimator()


    private val paintButton = Paint()

    private val paintCircle = Paint()

    //text in the button
    private val paintText = Paint().apply {
        textSize = 45f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }


    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {

            ButtonState.Loading -> {
                buttonText = " LOADING..."
                // button animation set up //.ofFloat(0f, measuredWidth.toFloat())
                //buttonAnimator = ObjectAnimator()
                    buttonAnimator.apply {
                        setObjectValues(0F,2500F)
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
                    setObjectValues(0F,2500F)
                    duration = 2000
                    repeatMode = ValueAnimator.RESTART
                    repeatCount = ValueAnimator.INFINITE

                    interpolator = AccelerateInterpolator(1f)
                    addUpdateListener {
                        loadingAngle = animatedValue as Float
                        this@LoadingButton.invalidate()
                    }
                    start()
                }
            }


            ButtonState.Completed -> {
                buttonText = "FINISHED"
                progress = 0f
                loadingAngle = 0f
                buttonAnimator.end()
                circleAnimator.end()
            }
            else -> {
                ButtonState.Clicked
                buttonText = "DOWNLOAD"
                progress = 1f
                loadingAngle = 1f
            }
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        when (buttonState) {
            buttonState -> ButtonState.Clicked
            buttonState -> ButtonState.Loading
            else -> ButtonState.Completed
        }
        invalidate()
        return true
    }

    init {
        //isClickable = true
        buttonText = "DOWNLOAD"
        buttonState = ButtonState.Clicked

        //Use  https://developer.android.com/reference/kotlin/android/util/AttributeSet

        context.theme.obtainStyledAttributes(attrs, R.styleable.customButton, 0, 0).apply {
            buttonTextColor = getColor(R.styleable.customButton_customButtonBackgroundColor, 0)
            buttonBackgroundColor = getColor(R.styleable.customButton_primaryBackgroundColor, 0)
            circleColor = getColor(R.styleable.customButton_circleColor,0)
        }
        paintButton.color = buttonBackgroundColor
        paintText.color = buttonTextColor
        paintCircle.color = circleColor
    }

    override fun onDraw(canvas: Canvas?) {
        paintButton.color = buttonBackgroundColor

        super.onDraw(canvas)
        canvas!!.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintButton)
        //canvas.drawText(buttonText,(measuredWidth/2).toFloat(),(measuredHeight/2).toFloat(),paintText)
        //button loadingColor
        paintButton.color = context.getColor(R.color.colorPrimaryDark)
        canvas.drawRect(0f, 0f, progress, measuredHeight.toFloat(), paintButton)

        canvas.drawText(
            buttonText,
            measuredWidth.toFloat() / 2,
            measuredHeight / 1.7f,
            paintText
        )
        canvas.drawArc(
            measuredWidth - 100f,
            (measuredHeight / 2) - 30f,
            measuredWidth - 50f,
            (measuredHeight / 2) + 30f,
            0f, loadingAngle, true, paintCircle
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
        w
        h
        setMeasuredDimension(w, h)
    }

    fun setState(state: ButtonState) {
        buttonState = state
    }
}