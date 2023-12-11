package me.aluceps.liveui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

enum class ConstraintTo {
    ON_ROOT,
    ON_SCREEN,
    UNDER_SCREEN,
}

fun View.constraintTo(
    root: ConstraintLayout,
    player: View,
    margin: Int,
    constraintTo: ConstraintTo
) {
    ConstraintSet().apply {
        clone(root)
        clear(id, ConstraintSet.BOTTOM)
        when (constraintTo) {
            ConstraintTo.ON_SCREEN -> {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    player.id,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
            ConstraintTo.UNDER_SCREEN -> {
                connect(
                    id,
                    ConstraintSet.TOP,
                    player.id,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
            ConstraintTo.ON_ROOT -> {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
        }
        applyTo(root)
    }
}

fun View.constraintToKeyboard(
    root: ConstraintLayout,
    margin: Int,
    constraintTo: ConstraintTo
) {
    ConstraintSet().apply {
        clone(root)
        when (constraintTo) {
            ConstraintTo.ON_ROOT -> {
                // nothing
            }
            ConstraintTo.ON_SCREEN -> {
                // nothing
            }
            ConstraintTo.UNDER_SCREEN -> {
                clear(this@constraintToKeyboard.id, ConstraintSet.TOP)
            }
        }
        connect(
            this@constraintToKeyboard.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            margin
        )
        connect(
            this@constraintToKeyboard.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            margin
        )
        applyTo(root)
    }
}

fun View.constraintToScreen(
    root: ConstraintLayout,
    screen: View,
    button: View,
    margin: Int,
    constraintTo: ConstraintTo
) {
    ConstraintSet().apply {
        clone(root)
        when (constraintTo) {
            ConstraintTo.ON_ROOT -> {
                connect(
                    this@constraintToScreen.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
            ConstraintTo.ON_SCREEN -> {
                connect(
                    this@constraintToScreen.id,
                    ConstraintSet.BOTTOM,
                    screen.id,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
            ConstraintTo.UNDER_SCREEN -> {
                clear(this@constraintToScreen.id, ConstraintSet.BOTTOM)
                connect(
                    this@constraintToScreen.id,
                    ConstraintSet.TOP,
                    screen.id,
                    ConstraintSet.BOTTOM,
                    margin
                )
            }
        }
        connect(
            this@constraintToScreen.id,
            ConstraintSet.END,
            button.id,
            ConstraintSet.START,
            margin
        )
        applyTo(root)
    }
}

