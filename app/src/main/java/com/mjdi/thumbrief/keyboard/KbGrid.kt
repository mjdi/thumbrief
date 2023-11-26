package com.mjdi.thumbrief.keyboard

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PorterDuff
import com.mjdi.thumbrief.common.Brushes
import com.mjdi.thumbrief.common.Constants
import com.mjdi.thumbrief.common.Measures
import java.lang.System.*


class KbGrid {

    private var cs = Constants()
    private var ms = Measures()
    private var bs = Brushes()

    lateinit var thumbToCornerToPoint : HashMap<Constants.THUMB,HashMap<Constants.CORNER,Pair<Double,Double>>>
    lateinit var thumbToRowToColumnToKbKey : HashMap<Constants.THUMB,HashMap<Constants.ROW,HashMap<Constants.COLUMN,KbKey>>>
    lateinit var thumbToGridMaskBitmap : HashMap<Constants.THUMB,Bitmap>
    init {
        createGrid()
    }

    private fun createGrid(){
        thumbToCornerToPoint = getThumbToCornerToPoint()
        thumbToRowToColumnToKbKey = getThumbToRowToColumnToKbKey(KbKey.KEYSET.LETTERS, thumbToCornerToPoint)
        thumbToGridMaskBitmap = getThumbToGridMaskBitmap(thumbToCornerToPoint, thumbToRowToColumnToKbKey)
    }
    @JvmName("kotlin_getThumbToCornerToPoint")

    private fun getThumbToCornerToPoint() : HashMap<Constants.THUMB,HashMap<Constants.CORNER,Pair<Double,Double>>> {
        val hmap = HashMap<Constants.THUMB,HashMap<Constants.CORNER,Pair<Double,Double>>>()

        // construct a 5 row by 9 or 10 column grid where the middle column is shared by both thumbs if Constants.SHARE_MIDDLE_COLUMN == true
        for (thumb in Constants.THUMB.entries) {
            hmap[thumb] = HashMap<Constants.CORNER,Pair<Double,Double>>()

            val topLeftPoint = Pair(
                (cs.NUM_COLS - if (cs.SHARED_MIDDLE_COLUMN) 1 else 0) *
                        if (thumb == Constants.THUMB.RIGHT) ms.columnWidth else 0.0,
                (ms.displayHeight - ms.navigationBarHeight - cs.NUM_ROWS * ms.rowHeight)
            )

            for (corner in Constants.CORNER.entries) {
                hmap[thumb]?.set(corner, Pair(
                        topLeftPoint.first  + cs.NUM_COLS * 1.0 * ms.columnWidth *
                                ms.cornerToUnitTranslation[corner]!!.first,
                        topLeftPoint.second + cs.NUM_ROWS * 1.0 * ms.rowHeight   *
                                ms.cornerToUnitTranslation[corner]!!.second
                    )
                )
            }
        }
        return hmap
    }
    @JvmName("kotlin_getThumbToRowToColumnToKbKey")
    private fun getThumbToRowToColumnToKbKey(
        keySet: KbKey.KEYSET,
        thumbToCornerToPoint: HashMap<Constants.THUMB, HashMap<Constants.CORNER, Pair<Double, Double>>>
        ) :
            HashMap<Constants.THUMB,HashMap<Constants.ROW,HashMap<Constants.COLUMN,KbKey>>> {
        val hmap = HashMap<Constants.THUMB,HashMap<Constants.ROW,HashMap<Constants.COLUMN,KbKey>>>()


        for (thumb in enumValues<Constants.THUMB>()) {
            hmap[thumb] = HashMap<Constants.ROW,HashMap<Constants.COLUMN,KbKey>>()
            var keyMapString = cs.thumbToGridKeyMapString[thumb]

            val bottomOutsideCorner = if (thumb == Constants.THUMB.LEFT) Constants.CORNER.BOTTOM_LEFT else Constants.CORNER.BOTTOM_RIGHT
            val bottomOutsideCornerPoint : Pair<Double, Double>? = thumbToCornerToPoint[thumb]?.get(bottomOutsideCorner)

            // trahslate point to top_left corner of cell, for ROW1, COL1
            val topLeftR1C1CellPoint = Pair(
                -(if (thumb == Constants.THUMB.RIGHT) ms.columnWidth else 0.0) +
                        bottomOutsideCornerPoint?.first!!,
                -(ms.rowHeight) + bottomOutsideCornerPoint.second
            )

            // iterate through keyMapString row by row, col by col, and generate the 4 corner points
            for ((r, row) in enumValues<Constants.ROW>().withIndex()) {
                hmap[thumb]?.set(row, HashMap<Constants.COLUMN,KbKey>())

                var rowString = keyMapString?.substring(0,cs.NUM_ROWS)

                for ((c, col) in enumValues<Constants.COLUMN>().withIndex()) {
                    val colString = rowString!!.substring(0,1)

                    val cellTopLeftPoint = Pair(
                        topLeftR1C1CellPoint.first  + // going from outer to inner columns
                            (if (thumb == Constants.THUMB.LEFT) 1.0 else -1.0) * c * ms.columnWidth,
                        topLeftR1C1CellPoint.second - r * ms.rowHeight // going up rows
                    )

                    val cornerToPoint = HashMap<Constants.CORNER, Pair<Double,Double>>()

                    for (corner in Constants.CORNER.entries) {
                        cornerToPoint.set(
                            corner, Pair(
                                cellTopLeftPoint.first +
                                        ms.columnWidth * ms.cornerToUnitTranslation[corner]!!.first,
                                cellTopLeftPoint.second +
                                        ms.rowHeight * ms.cornerToUnitTranslation[corner]!!.second
                            )
                        )
                    }

                    hmap[thumb]?.get(row)?.set(col, KbKey(
                        colString,
                        keySet,
                        thumb,
                        row,
                        col,
                        cornerToPoint
                        )
                    )

                    if (rowString.length != 1) // can't substring beyond a 1 letter rowString
                        rowString = rowString.substring(1,rowString.length)
                }
                if (keyMapString?.length != cs.NUM_COLS) //can't substring past NUM_COL keyMapString
                    keyMapString = keyMapString?.substring(cs.NUM_COLS,keyMapString.length)
            }
        }
        return hmap
    }
    @JvmName("kotlin_getThumbToGridMaskBitmapt")
    private fun getThumbToGridMaskBitmap(
        thumbToCornerToPoint: HashMap<Constants.THUMB,HashMap<Constants.CORNER,Pair<Double,Double>>>,
        thumbToRowToColumnToKbKey: HashMap<Constants.THUMB, HashMap<Constants.ROW, HashMap<Constants.COLUMN, KbKey>>>) :
            HashMap<Constants.THUMB,Bitmap> {

        val hmap = HashMap<Constants.THUMB,Bitmap>()

        for (thumb in enumValues<Constants.THUMB>()) { // per thumb
            // background
            //val squarebackgroundBitmap = Bitmap.createBitmap(ms.sideLengthInt, ms.sideLengthInt, bs.BITMAP_CONFIG_ALPHA)
            //var squarebackgroundCanvas = Canvas(squarebackgroundBitmap)
            //squarebackgroundCanvas.drawColor(bs.GRID_BACKGROUND_COLOR,PorterDuff.Mode.ADD)

            // dgrid outline
            val squareBitmap = Bitmap.createBitmap(ms.sideLengthInt, ms.sideLengthInt, bs.BITMAP_CONFIG_ALPHA)
            var squareCanvas = Canvas(squareBitmap)
            //squareCanvas.drawBitmap(squarebackgroundBitmap, 0f, 0f, null)

            val topLeftPoint = tranformPointToBitmapFrameOfReference(thumb, thumbToCornerToPoint[thumb]?.get(Constants.CORNER.TOP_LEFT)!!)
            val bottomLeftPoint = tranformPointToBitmapFrameOfReference(thumb, thumbToCornerToPoint[thumb]?.get(Constants.CORNER.BOTTOM_LEFT)!!)
            val bottomRightPoint = tranformPointToBitmapFrameOfReference(thumb, thumbToCornerToPoint[thumb]?.get(Constants.CORNER.BOTTOM_RIGHT)!!)

            squareCanvas.drawRect(0f, 0f, ms.sideLengthFloat, ms.sideLengthFloat, bs.gridLinePaint) // outer rectangle

            // grid column separators
            for (i in 0..<cs.NUM_COLS) {
                val offset = (i * ms.columnWidth).toFloat()
                squareCanvas.drawLine(
                    topLeftPoint.first.toFloat() + offset,
                    topLeftPoint.second.toFloat(),
                    bottomLeftPoint.first.toFloat() + offset,
                    bottomLeftPoint.second.toFloat(),
                    bs.gridLinePaint
                )
            }
            // grid row separators
            for (i in 0..<cs.NUM_ROWS) {
                val offset = (i * ms.rowHeight).toFloat()
                squareCanvas.drawLine(
                    bottomLeftPoint.first.toFloat(),
                    bottomLeftPoint.second.toFloat() - offset,
                    bottomRightPoint.first.toFloat(),
                    bottomRightPoint.second.toFloat() - offset,
                    bs.gridLinePaint
                )
            }
            // text in the center of each grid cell, translated halfway down by fontsize
            for (row in enumValues<Constants.ROW>()) {
                for (col in enumValues<Constants.COLUMN>()) {
                    val kbKey = thumbToRowToColumnToKbKey[thumb]?.get(row)?.get(col)
                    val vertDisplacement: Float = -((bs.gridTextPaint.descent() + bs.gridTextPaint.ascent()) / 2.0f)
                    val textPath = Path()

                    val topLeftPoint = tranformPointToBitmapFrameOfReference(thumb, kbKey?.cornerToPoint?.get(Constants.CORNER.TOP_LEFT)!!)
                    val topRightPoint = tranformPointToBitmapFrameOfReference(thumb, kbKey?.cornerToPoint?.get(Constants.CORNER.TOP_RIGHT)!!)
                    val bottomLeftPoint = tranformPointToBitmapFrameOfReference(thumb, kbKey?.cornerToPoint?.get(Constants.CORNER.BOTTOM_LEFT)!!)

                    val keyCenterPoint = Pair(
                        (topLeftPoint.first + topRightPoint.first) / 2.0,
                        (topLeftPoint.second + bottomLeftPoint.second) / 2.0,
                    )
                    textPath.moveTo( // halfway left
                        keyCenterPoint.first.toFloat() - ms.columnWidth.toFloat() / 2.0f,
                        keyCenterPoint.second.toFloat())

                    textPath.lineTo( // to halfway right
                        keyCenterPoint.first.toFloat() + ms.columnWidth.toFloat() / 2.0f,
                        keyCenterPoint.second.toFloat()
                    )

                    if (kbKey != null) {
                        squareCanvas.drawTextOnPath(kbKey.keyString, textPath, 0f, vertDisplacement, bs.gridTextPaint)
                    }

                }
            }
            squareCanvas.save()
            squareCanvas.drawBitmap(squareBitmap, 0f, 0f, bs.gridLinePaint)
            hmap[thumb] = squareBitmap
        }
        return hmap
    }

    private fun tranformPointToBitmapFrameOfReference(
        thumb: Constants.THUMB, point: Pair<Double,Double>) : Pair<Double,Double> {
        var x = point.first -
                if (thumb == Constants.THUMB.RIGHT)
                        (if (cs.SHARED_MIDDLE_COLUMN) cs.NUM_COLS - 1
                        else cs.NUM_COLS) *
                                ms.columnWidth
                else 0.0
        var y = point.second + ms.sideLengthFloat - ms.displayHeight + ms.navigationBarHeight
        return Pair(x,y)
    }

    fun drawGrid(canvas: Canvas){
        for (thumb in enumValues<Constants.THUMB>()) {
            val bitmap = thumbToGridMaskBitmap[thumb]
            val topLeftCorner = thumbToCornerToPoint[thumb]?.get(Constants.CORNER.TOP_LEFT)
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, topLeftCorner?.first!!.toFloat(), 0f, null)
            }
        }
    }
    fun createGridTap(x: Double, y:Double) : GridTap {
       return GridTap(Pair(x,y), currentTimeMillis())
    }
}