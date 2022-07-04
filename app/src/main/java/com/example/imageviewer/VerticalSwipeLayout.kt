package com.example.customeimageviewer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.fragment.app.Fragment

class VerticalSwipeLayout(context: Context) : FrameLayout(context) {

    companion object {
        lateinit var viewDragHelper: ViewDragHelper
        lateinit var fragment: Fragment
        lateinit var contentView: View
        var dragPercent = 0f
    }

    init {
        viewDragHelper = ViewDragHelper.create(this, DragHelper(context))
        viewDragHelper.minVelocity = 4000f
    }

    fun setFragment(f: Fragment, view: View): View {
        addView(view)
        fragment = f
        contentView = view
        return this
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val drawChild: Boolean = try {
            super.drawChild(canvas, child, drawingTime)
        } catch (e: StackOverflowError) {
            e.printStackTrace()
            return false
        }
        invalidate()
        return drawChild
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel()
            return false
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    class DragHelper(val context: Context) : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == contentView
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val topBound: Int = child.paddingTop
            val bottomBound = Math.min(child.getHeight(), Math.max(top, 0))
            return Math.min(Math.max(top, topBound), bottomBound)
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            dragPercent = top.toFloat() / contentView.height
            if (dragPercent <= 0.5) {
                val newDrag = if (dragPercent < 0.9) dragPercent * 2 else dragPercent
                contentView.setBackgroundColor(Color.parseColor("#" + ConvertDecToHex.decToHex((255 * (1 - newDrag)).toInt()) + "000000"))
            }
            if (dragPercent > 0.8) {
                fragment.fragmentManager?.popBackStackImmediate()
            }
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val top =
                if ((yvel > 0 || yvel == 0f) && dragPercent > 0.3f) releasedChild.height + 20 else 0
            viewDragHelper.settleCapturedViewAt(0, top)
        }
    }

    private object ConvertDecToHex {
        private const val sizeOfIntInHalfBytes = 2
        private const val numberOfBitsInAHalfByte = 4
        private const val halfByte = 0x0F
        private val hexDigits = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )

        fun decToHex(dec: Int): String {
            var dec = dec
            val hexBuilder = StringBuilder(sizeOfIntInHalfBytes)
            hexBuilder.setLength(sizeOfIntInHalfBytes)
            for (i in sizeOfIntInHalfBytes - 1 downTo 0) {
                val j = dec and halfByte
                hexBuilder.setCharAt(i, hexDigits[j])
                dec = dec shr numberOfBitsInAHalfByte
            }
            return hexBuilder.toString()
        }
    }
}

