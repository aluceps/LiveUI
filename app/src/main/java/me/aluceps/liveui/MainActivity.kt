package me.aluceps.liveui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import me.aluceps.liveui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val margin by lazy { resources.getDimensionPixelSize(R.dimen.spacing_8dp) }

    private val keyboardDetector = KeyboardDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardDetector.stop(this)
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
            binding.reaction.constraintTo(
                root = binding.root,
                player = binding.player,
                margin = margin,
                constraintTo = it
            )
            binding.comment.constraintTo(
                root = binding.root,
                player = binding.player,
                margin = margin,
                constraintTo = it
            )

            keyboardDetector.start(
                activity = this,
                onShow = {
                    binding.comment.constraintToKeyboard(
                        root = binding.root,
                        margin = margin,
                        constraintTo = it
                    )
                },
                onHide = {
                    binding.comment.constraintToScreen(
                        root = binding.root,
                        screen = binding.player,
                        button = binding.reaction,
                        margin = margin,
                        constraintTo = it
                    )
                },
            )
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
