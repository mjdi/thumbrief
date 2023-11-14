package com.mjdi.thumbrief.keyboard

import android.content.Context
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View

class KbView @JvmOverloads constructor(context: Context): View(context) {

    private lateinit var onKbActionListener: OnKbActionListener
    var grid : KbGrid = KbGrid()
    private var pIndexToGridTap: HashMap<Int, GridTap> = HashMap<Int, GridTap>()

    override fun onTouchEvent(me: MotionEvent): Boolean {
        var consumed = false // assume not consumed unless otherwise determined consumed;
        when (me.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> consumed = motionEventGeneralActionDown(me)
            MotionEvent.ACTION_POINTER_DOWN -> consumed = motionEventGeneralActionDown(me)
            MotionEvent.ACTION_MOVE -> motionEventActionMove(me)
            MotionEvent.ACTION_UP -> motionEventGeneralActionUp(me)
            MotionEvent.ACTION_POINTER_UP -> motionEventGeneralActionUp(me)
        }
        return consumed
    }

    private fun motionEventGeneralActionDown(motionEvent: MotionEvent) : Boolean {
        val pIndex: Int = motionEvent.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr
                MotionEvent.ACTION_POINTER_INDEX_SHIFT
        val pIdentity: Int = motionEvent.getPointerId(pIndex)
        val x = motionEvent.getX(pIndex).toDouble()
        val y = motionEvent.getY(pIndex).toDouble()
        var gridTap = grid.createGridTap(x,y)
        pIndexToGridTap[pIdentity] = gridTap

        return true
    }

    private fun motionEventActionMove(motionEvent: MotionEvent) {

    }

    private fun motionEventGeneralActionUp(motionEvent: MotionEvent) {
        val pIndex: Int = motionEvent.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr
                MotionEvent.ACTION_POINTER_INDEX_SHIFT
        val pIdentity: Int = motionEvent.getPointerId(pIndex)

        if (pIndexToGridTap.containsKey(pIdentity)) {
            onKbActionListener.onKbAction(KbAction(KeyEvent.KEYCODE_1,0))
        }
        pIndexToGridTap.remove(pIndex)
    }

    interface OnKbActionListener {
        fun onKbAction(action: KbAction)
    }

    fun setKbActionListener(onKbActionListenerArg : OnKbActionListener) {
        onKbActionListener = onKbActionListenerArg
    }
}

