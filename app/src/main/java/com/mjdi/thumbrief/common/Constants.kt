package com.mjdi.thumbrief.common

class Constants {

    val SHARED_MIDDLE_COLUMN = true // determines if both thumbs share the middle column of letters
    val NUM_ROWS = 5
    val NUM_COLS = 5
    val leftThumbKeyMapString = "gdlmyniorptaecbfhsuxwvkjq"
    val rightThumbKeyMapString = leftThumbKeyMapString.replace('j','z')
    var thumbToGridKeyMapString = HashMap<THUMB,String>()

    init {
        // from bottom row to top row, outside column to inside column
        thumbToGridKeyMapString[THUMB.LEFT] = leftThumbKeyMapString
        thumbToGridKeyMapString[THUMB.RIGHT] = rightThumbKeyMapString
    }

    enum class THUMB {
        LEFT,
        RIGHT
    }

    enum class CORNER {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    enum class ROW { // Rows are counted from bottom to top
        ROW1,
        ROW2,
        ROW3,
        ROW4,
        ROW5,
    }

    enum class COLUMN { // Columns are counted from outside to inside
        COL1,
        COL2,
        COL3,
        COL4,
        COL5,
    }
}