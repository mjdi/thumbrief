package com.mjdi.thumbrief.keyboard

import android.content.Context
import android.graphics.Canvas
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.mjdi.thumbrief.common.Brushes
import com.mjdi.thumbrief.common.Constants


class KbView @JvmOverloads constructor(
    context: Context,
    ): View(context) {

    private lateinit var onKbActionListener: OnKbActionListener
    private var grid : KbGrid = KbGrid()
    private lateinit var gridTapResolver : GridTapResolver


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
        gridTapResolver.addGridTap(motionEvent)
        return true // this lets the underlying app know that the touch was consumed by the ime
    }

    private fun motionEventActionMove(motionEvent: MotionEvent) {
        gridTapResolver.moveGridTap(motionEvent)
    }

    private fun motionEventGeneralActionUp(motionEvent: MotionEvent) {
        gridTapResolver.liftGridTap(motionEvent)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            grid.drawGrid(canvas)
        }
    }
    interface OnKbActionListener {
        fun onKbAction(action: KbAction)
    }

    fun setKbActionListener(onKbActionListenerArg : OnKbActionListener) {
        onKbActionListener = onKbActionListenerArg
        gridTapResolver = GridTapResolver(onKbActionListener, grid)
    }
}

