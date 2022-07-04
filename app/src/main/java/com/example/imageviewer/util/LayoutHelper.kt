package com.example.imageviewer.util

import android.widget.FrameLayout

class LayoutHelper {
    companion object {
        const val MATCH_PARENT = -1
        const val WRAP_CONTENT = -2
        private fun getSize(size: Int): Int {
            return (if (size < 0) size else size.dp())
        }

        fun createFrame(
            width: Int,
            height: Int,
            gravity: Int,
            leftMargin: Int,
            topMargin: Int,
            rightMargin: Int,
            bottomMargin: Int
        ): FrameLayout.LayoutParams {
            val layoutParams =
                FrameLayout.LayoutParams(getSize(width), getSize(height), gravity)
            layoutParams.setMargins(
                leftMargin.dp(),
                topMargin.dp(),
                rightMargin.dp(),
                bottomMargin.dp()
            )
            return layoutParams
        }

        fun createFrame(width: Int, height: Int, gravity: Int): FrameLayout.LayoutParams {
            return FrameLayout.LayoutParams(
                getSize(width),
                getSize(height),
                gravity
            )
        }

        fun createFrame(width: Int, height: Int): FrameLayout.LayoutParams {
            return FrameLayout.LayoutParams(
                getSize(width),
                getSize(height)
            )
        }
    }
}