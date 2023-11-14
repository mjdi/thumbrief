package com.mjdi.thumbrief.keyboard

import com.mjdi.thumbrief.keyboard.GridTap
import java.lang.System.*

class KbGrid {
    fun createGridTap(x: Double, y:Double) : GridTap {
       return GridTap(Pair<Double,Double>(x,y), currentTimeMillis())
    }
}