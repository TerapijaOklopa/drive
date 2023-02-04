package com.mobile.drive.mobile.utils

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher

/**
 * Utility class for delayed input text watcher
 */
class DelayedTextWatcher(
    private val callback: Callback?,
    private val delay: Long = 500
) : TextWatcher {

    private var text = ""
    private var timer: Handler? = null
    private val runnable = Runnable {
        callback?.onTextChange(text)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // nothing to do here
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        resetTimer()
    }

    override fun afterTextChanged(s: Editable?) {
        text = s.toString()
        resetTimer()
        // user typed start timer
        timer = Handler(Looper.getMainLooper())
        timer?.postDelayed(runnable, delay)
    }

    private fun resetTimer() {
        timer?.let {
            it.removeCallbacks(runnable)
            timer = null
        }
    }

    interface Callback {
        fun onTextChange(text: String)
    }
}
