package com.mjdi.thumbrief.common

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint

class Brushes {

    private val ms = Measures()

    val GRID_BACKGROUND_COLOR = Color.BLACK

    val BITMAP_CONFIG_ALPHA = Bitmap.Config.ALPHA_8
    val BITMAP_CONFIG_ARGB_8888 = Bitmap.Config.ARGB_8888

    private val GRID_LINE_FLAGS = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    private val GRID_LINE_STYLE = Paint.Style.STROKE
    private val GRID_LINE_STROKE_WIDTH = 5.0f * ms.pixelDensity
    private val GRID_LINE_STROKE_CAP = Paint.Cap.BUTT
    private val GRID_LINE_COLOR = Color.BLACK

    private val GRID_TEXT_FLAGS = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    private val GRID_TEXT_COLOR = Color.BLACK
    private val GRID_TEXT_SIZE = 40.0f * ms.pixelDensity
    private val GRID_TEXT_ALIGN = Paint.Align.CENTER

    val gridLinePaint = Paint(GRID_LINE_FLAGS)
    val gridTextPaint = Paint(GRID_TEXT_FLAGS)

    init {
        gridLinePaint.style = GRID_LINE_STYLE
        gridLinePaint.strokeWidth = GRID_LINE_STROKE_WIDTH
        gridLinePaint.strokeCap = GRID_LINE_STROKE_CAP
        gridLinePaint.color = GRID_LINE_COLOR

        gridTextPaint.color = GRID_TEXT_COLOR
        gridTextPaint.textSize = GRID_TEXT_SIZE
        gridTextPaint.textAlign = GRID_TEXT_ALIGN
    }
}