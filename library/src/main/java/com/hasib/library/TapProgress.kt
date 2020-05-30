package com.hasib.library

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*


class TapProgress(context: Context?, attrs: AttributeSet?) : View(context, attrs),
    View.OnTouchListener {

    private var task: TimerTask? = null
    private var timer: Timer? = null
    private var textPaint: Paint
    private var heightRect: Float = 0f
    private var startAngle: Float = 0f
    private var startTime: Long = 0
    private var isOnTouch = false
    private var distanceBetweenArc = 30
    private var strokeWidth = 10f
    private var duration= 2000
    private var shadow = true
    private var solidArcColor = ContextCompat.getColor(context!!, R.color.colorAccent)
    private var strokeArcColor = ContextCompat.getColor(context!!, R.color.colorAccent)

    private var tapProgressCompletedListener: TapProgressCompletedListener? =null

    init {
        val attributes =
            context!!.obtainStyledAttributes(attrs, R.styleable.TapProgress)
        distanceBetweenArc = attributes.getDimensionPixelSize(R.styleable.TapProgress_tpArc_Distance, 30)
        strokeWidth = attributes.getDimension(R.styleable.TapProgress_tpArc_strokeWidth, 10f)
        duration = attributes.getInteger(R.styleable.TapProgress_tpArc_duration, 2000)
        shadow = attributes.getBoolean(R.styleable.TapProgress_tpArc_shoadow, true)
        solidArcColor = attributes.getColor(
            R.styleable.TapProgress_tpArc_solidArcColor, ContextCompat.getColor(context,
                R.color.colorAccent
            ))
        strokeArcColor = attributes.getColor(
            R.styleable.TapProgress_tpArc_strokeArcColor, ContextCompat.getColor(context,
                R.color.colorAccent
            ))
    }

    fun setTapProgressCompletedListener(listener: TapProgressCompletedListener){
        this.tapProgressCompletedListener = listener
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            onTouchActionDown()
        } else if (event?.action == MotionEvent.ACTION_UP) {
            onTouchActionUp()
            return true
        }
        if (isOnTouch) {
            if (startTime == 0L) {
                startTime = System.currentTimeMillis()
            }
        }
        return true
    }

    private fun onTouchActionUp() {
        startTime = 0
        startAngle = 0f
        isOnTouch = false
        cancelTask()
        invalidate()
    }

    private fun onTouchActionDown() {
        isOnTouch = true
        runTimerOnTouchDown()
    }

    private fun runTimerOnTouchDown() {
        timer = Timer()
        task = object : TimerTask() {
            override fun run() {
                updateProgress()
            }

        }
        timer?.scheduleAtFixedRate(task, 5L, 5L)
    }

    private fun updateProgress() {
        var diff = System.currentTimeMillis() - startTime
        if (diff > duration) {
            tapProgressCompletedListener?.onTapProgressCompleted()
            cancelTask()
            return
        }

        startAngle = (diff / duration.toFloat()) * 180
        //                    Log.d("TAG", "angle: " + startAngle)
        (context as AppCompatActivity).runOnUiThread {
            invalidate()
        }
    }

    private fun cancelTask() {
        timer?.purge()
        timer?.cancel()
        task?.cancel()
    }

    var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var solidPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var solidPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    var animator: ValueAnimator? = null

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = strokeArcColor
        solidPaint.color = solidArcColor
        solidPaint2.color = solidArcColor
        if(shadow) {
            solidPaint.setShadowLayer(18f, 0f, 0f, R.color.colorPrimary)
        }

        textPaint = Paint()
        textPaint.setColor(Color.WHITE)
        textPaint.textSize = 60f
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        var widthRect = width
        val heightR = (height.toFloat() + 60f) - 60
        var scale = (height.toFloat() - 60) / (width - 200f)
        heightRect = widthRect * scale
        canvas?.drawArc(
            -130f,//70f,
            strokeWidth / 2 ,
            width.toFloat() + 130,//width.toFloat() - 70,
            height.toFloat() +  (height.toFloat()) - (strokeWidth / 2) ,//height.toFloat(), //heightRect + 20,
            180f,
            startAngle,
            false,
            paint
        )
        canvas?.drawArc(
            -130f + distanceBetweenArc,
            (strokeWidth) + distanceBetweenArc,
            width.toFloat() + 130 - distanceBetweenArc,
            height.toFloat() +  (height.toFloat()) - (strokeWidth + distanceBetweenArc),
            0f,
            -180f,
            false,
            solidPaint
        )
//        val text = "Tap to progress"
//        var r = Rect()
//        textPaint.setTextAlign(Paint.Align.LEFT)
//        textPaint.getTextBounds(text, 0, text.length, r)
//        val textX = width / 2f - (r.width()/2)
//        canvas?.drawText(text, textX, heightR/4f + 60 + r.height()/2, textPaint)

//        var widthRect = width + 40
//        var scale = ((height.toFloat()/2 - 100f) - 60)/ (width)
//        heightRect = widthRect * scale
//
//        canvas?.drawArc(-20f, 40f, width.toFloat() + 20, heightRect+40, 180f, startAngle, false, paint)
//        canvas?.drawArc(0f, 60f, width.toFloat(), height.toFloat()/2 - 100f, 0f, -180f, false, solidPaint)

//        canvas?.drawRect(0f, 100f, width.toFloat(), 250f, solidPaint)
//        canvas?.drawArc(0f, 50f, width.toFloat(), 150f, 180f, 180f, false, solidPaint2)
    }

    interface TapProgressCompletedListener{
        fun onTapProgressCompleted()
    }
}