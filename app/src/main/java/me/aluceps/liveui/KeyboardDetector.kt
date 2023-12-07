package me.aluceps.liveui

import android.app.Activity
import android.view.View
import android.view.ViewTreeObserver

class KeyboardDetector {
    private var listener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var screenHeight = 0

    private var passedShow = false
    private var passedHide = false

    fun start(
        activity: Activity,
        onShow: () -> Unit,
        onHide: () -> Unit,
    ) {
        if (listener != null) return

        val contentView = activity.findViewById<View>(android.R.id.content)

        listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (contentView.height == 0) {
                    return
                }

                if (screenHeight == 0) {
                    screenHeight = contentView.height
                }

                when {
                    screenHeight == contentView.height -> {
                        if (passedShow) return
                        onHide()
                        passedShow = true
                        passedHide = false
                    }
                    else -> {
                        if (passedHide) return
                        onShow()
                        passedShow = false
                        passedHide = true
                    }
                }

                contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (listener != null) contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
            }
        }

        contentView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    fun stop(activity: Activity) {
        val contentView = activity.findViewById<View>(android.R.id.content)

        contentView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        listener = null
    }
}
