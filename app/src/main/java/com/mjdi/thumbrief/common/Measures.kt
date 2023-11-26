package com.mjdi.thumbrief.common

import android.content.res.Resources

class Measures {

    // the origin of the display is considered to be the top left in terms of the Canvas
    private var cs = Constants()
    var orientation : Int
    var pixelDensity : Float
    var displayHeight : Int
    var displayWidth : Int
    var statusBarHeight : Int
    var navigationBarHeight : Int
    var columnWidth : Double
    var rowHeight : Double
    var sideLengthFloat : Float
    var sideLengthInt : Int
    var cornerToUnitTranslation = HashMap<Constants.CORNER,Pair<Int,Int>>()

    init {
        cs = Constants()
        orientation = Resources.getSystem().configuration.orientation
        pixelDensity = Resources.getSystem().displayMetrics.density
        displayWidth = Resources.getSystem().displayMetrics.widthPixels
        displayHeight = Resources.getSystem().displayMetrics.heightPixels
        statusBarHeight = getStatusBarHeight()
        navigationBarHeight = getNavigationBarHeight()
        // we want square keys
        columnWidth = getColumnWidth(displayWidth)
        rowHeight = columnWidth
        // assuming a square, get the side length
        sideLengthFloat = rowHeight.toFloat() * cs.NUM_ROWS.toFloat()
        sideLengthInt = sideLengthFloat.toInt()

        cornerToUnitTranslation[Constants.CORNER.TOP_LEFT] = Pair(0,0)
        cornerToUnitTranslation[Constants.CORNER.TOP_RIGHT] = Pair(1,0)
        cornerToUnitTranslation[Constants.CORNER.BOTTOM_RIGHT] = Pair(1,1)
        cornerToUnitTranslation[Constants.CORNER.BOTTOM_LEFT] = Pair(0,1)
    }

    @JvmName("kotlin_getStatusBarHeight")
    private fun getStatusBarHeight() : Int {
        var result = 0
        val resourceId: Int = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId)
        }
        return result
    }

    @JvmName("kotlin_getNavigationBarHeight")
    private fun getNavigationBarHeight() : Int {
        var result = 0
        val resourceId: Int = Resources.getSystem().getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId)
        }
        return result
    }

    @JvmName("kotlin_getColumnWidth")
    private fun getColumnWidth(displayWidth: Int) : Double {
        return displayWidth * 1.0 / (cs.NUM_COLS * 2 - if (cs.SHARED_MIDDLE_COLUMN) 1 else 0)
    }
}