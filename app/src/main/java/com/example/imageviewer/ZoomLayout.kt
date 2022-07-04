package com.example.customeimageviewer

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.FrameLayout
import kotlin.math.sign

@SuppressLint("ClickableViewAccessibility")
class ZoomLayout
    (context: Context) : FrameLayout(context), ScaleGestureDetector.OnScaleGestureListener {

    private var mode = Mode.NONE
    private var scale = 1.0f
    private var lastScaleFactor = 0f
    private var startX = 0f
    private var startY = 0f
    private var dx = 0f
    private var dy = 0f
    private var prevDx = 0f
    private var prevDy = 0f
    private var firstTouch = false
    private var time = System.currentTimeMillis()
    private var restore = false

    init {
        val scaleDetector = ScaleGestureDetector(context, this)
        setOnTouchListener { view: View, motionEvent: MotionEvent ->
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> if (firstTouch && System.currentTimeMillis() - time <= CLICK_TIME_DIFFERENCE_THRESHOLD) {
                    //do stuff here for double tap
                    if (restore) {
                        scale = 1.0f
                        restore = false
                    } else {
                        scale *= 2.0f
                        restore = true
                    }
                    mode = Mode.ZOOM
                    firstTouch = false
                } else {
                    if (scale > MIN_ZOOM) {
                        mode = Mode.DRAG
                        startX = motionEvent.x - prevDx
                        startY = motionEvent.y - prevDy
                    }
                    firstTouch = true
                    time = System.currentTimeMillis()
                }
                MotionEvent.ACTION_MOVE -> if (mode === Mode.DRAG) {
                    dx = motionEvent.x - startX
                    dy = motionEvent.y - startY
                }
                MotionEvent.ACTION_POINTER_DOWN -> mode = Mode.ZOOM
                MotionEvent.ACTION_POINTER_UP -> mode = Mode.NONE
                MotionEvent.ACTION_UP -> {
                    mode = Mode.NONE
                    prevDx = dx
                    prevDy = dy
                }
            }
            scaleDetector.onTouchEvent(motionEvent)
            if (mode === Mode.DRAG && scale >= MIN_ZOOM || mode === Mode.ZOOM) {
                parent.requestDisallowInterceptTouchEvent(true)
                val maxDx: Float =
                    (child().width - child().width / scale) / 2 * scale
                val maxDy: Float =
                    (child().height - child().height / scale) / 2 * scale
                dx = dx.coerceAtLeast(-maxDx).coerceAtMost(maxDx)
                dy = dy.coerceAtLeast(-maxDy).coerceAtMost(maxDy)
                applyScaleAndTranslation()
            }
            true
        }
    }

    private fun child(): View {
        return getChildAt(0)
    }

    private fun applyScaleAndTranslation() {
        child().scaleX = scale
        child().scaleY = scale
        child().translationX = dx
        child().translationY = dy
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scaleFactor: Float = detector.scaleFactor
        if (lastScaleFactor == 0f || sign(scaleFactor) == sign(lastScaleFactor)) {
            scale *= scaleFactor
            scale = MIN_ZOOM.coerceAtLeast(scale.coerceAtMost(MAX_ZOOM))
            lastScaleFactor = scaleFactor
        } else {
            lastScaleFactor = 0f
        }
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {}

    private enum class Mode {
        NONE,
        ZOOM,
        DRAG
    }

    companion object {
        const val CLICK_TIME_DIFFERENCE_THRESHOLD = 300
        const val MIN_ZOOM = 1.0f
        const val MAX_ZOOM = 4.0f
    }
}