package com.mjdi.thumbrief.service

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.mjdi.thumbrief.keyboard.KbAction
import com.mjdi.thumbrief.keyboard.KbView


class ImeService : InputMethodService(), KbView.OnKbActionListener {
    private lateinit var mInputMethodManager : InputMethodManager
    private lateinit var kbView : KbView

    override fun onCreate() {
        super.onCreate()
        mInputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    override fun onInitializeInterface() {
    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
    }

    override fun onConfigureWindow(win: Window?, isFullscreen: Boolean, isCandidatesOnly: Boolean) {
        //super.onConfigureWindow(win, isFullscreen, isCandidatesOnly)
        kbView = KbView(this)
        kbView.setKbActionListener(this)
    }

    override fun onComputeInsets(outInsets: Insets?) {
        super.onComputeInsets(outInsets)
    }

    override fun onCreateInputView(): View? {
        return kbView
    }

    override fun onKbAction(act: KbAction) {
        var now = System.currentTimeMillis()
        var ic: InputConnection = currentInputConnection
        ic.sendKeyEvent(
            KeyEvent(now, now, KeyEvent.ACTION_DOWN, act.charCode, 0, act.modifiers))
        ic.sendKeyEvent(
            KeyEvent(now, now, KeyEvent.ACTION_UP, act.charCode, 0, act.modifiers))

    }

    override fun onFinishInput() {
        super.onFinishInput()
        setCandidatesViewShown(false)
    }

    override fun onEvaluateFullscreenMode(): Boolean {
        return false // return super.onEvaluateFullscreenMode()
    }

}