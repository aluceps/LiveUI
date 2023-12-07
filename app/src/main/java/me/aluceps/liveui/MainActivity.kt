package me.aluceps.liveui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import me.aluceps.liveui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val margin by lazy { resources.getDimensionPixelSize(R.dimen.spacing_8dp) }

    private val keyboardDetector = KeyboardDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "hasFocus=$hasFocus")
        if (hasFocus) setupController()
    }

    private fun setupController() {
        val root = binding.root.height
        val content = binding.player.height
        // 上下のマージンを考慮
        val button = binding.reaction.height + margin * 2

        Log.d(TAG, "root=$root content=$content button=$button")

        when {
            // 動画プレイヤーが画面に収まらない
            root < content -> ConstraintTo.ON_ROOT
            // 動画プレイヤー下に予約が足りずコントローラーが収まらない
            root < (content + button) -> ConstraintTo.ON_SCREEN
            // 動画プレイヤーとボタンを含めた高さが画面に収まる
            root >= (content + button) -> ConstraintTo.UNDER_SCREEN
            else -> null
        }?.let {
            Log.d(TAG, "constraintTo: $it")
            binding.reaction.constraintTo(it)
            binding.comment.constraintTo(it)

            keyboardDetector.start(
                activity = this,
                onShow = {
                    binding.comment.constraintToKeyboard(
                        binding.root,
                        it
                    )
                },
                onHide = {
                    binding.comment.constraintToScreen(
                        binding.root,
                        binding.player,
                        binding.reaction,
                        it
                    )
                },
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardDetector.stop(this)
    }

    private fun View.constraintTo(constraintTo: ConstraintTo) {
        ConstraintSet().apply {
            clone(binding.root)
            clear(id, ConstraintSet.BOTTOM)
            when (constraintTo) {
                ConstraintTo.ON_SCREEN -> {
                    connect(
                        id,
                        ConstraintSet.BOTTOM,
                        binding.player.id,
                        ConstraintSet.BOTTOM,
                        margin
                    )
                }
                ConstraintTo.UNDER_SCREEN -> {
                    connect(
                        id,
                        ConstraintSet.TOP,
                        binding.player.id,
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
            applyTo(binding.root)
        }
    }

    private fun View.constraintToKeyboard(
        root: ConstraintLayout,
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

    private fun View.constraintToScreen(
        root: ConstraintLayout,
        screen: View,
        button: View,
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

    private enum class ConstraintTo {
        ON_ROOT,
        ON_SCREEN,
        UNDER_SCREEN,
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
