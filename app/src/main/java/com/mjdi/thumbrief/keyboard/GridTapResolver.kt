package com.mjdi.thumbrief.keyboard

import android.view.KeyEvent
import android.view.MotionEvent

class GridTapResolver (val onKbActionListener: KbView.OnKbActionListener, val grid: KbGrid) {
    private var pointIdentityToGridTap: HashMap<Int, GridTap> = HashMap()

    private fun getPointIndex(motionEvent : MotionEvent) : Int {
        return motionEvent.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    }
    private fun getPointIdentity(motionEvent: MotionEvent, pointIndex: Int) : Int {
        return motionEvent.getPointerId(pointIndex)
    }

    fun addGridTap (motionEvent: MotionEvent) {
        val pointIndex = getPointIndex(motionEvent)
        pointIdentityToGridTap[getPointIdentity(motionEvent, pointIndex)] = grid.createGridTap(
            motionEvent.getX(pointIndex).toDouble(),
            motionEvent.getY(pointIndex).toDouble()
        )
    }

    fun moveGridTap (motionEvent: MotionEvent) {

    }

    fun liftGridTap (motionEvent: MotionEvent) {
        val pointIndex = getPointIndex(motionEvent)
        if (pointIdentityToGridTap.containsKey(getPointIdentity(motionEvent, pointIndex))) {
            onKbActionListener.onKbAction(KbAction(KeyEvent.KEYCODE_1,0))
        }
        pointIdentityToGridTap.remove(pointIndex)
    }
}