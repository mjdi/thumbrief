package com.mjdi.thumbrief.keyboard

import com.mjdi.thumbrief.common.Constants


class KbKey(
    val keyString: String,
    val keySet: KEYSET,
    val thumb: Constants.THUMB,
    val row: Constants.ROW,
    val column: Constants.COLUMN,
    val cornerToPoint: HashMap<Constants.CORNER,Pair<Double,Double>>) {

    enum class KEYSET {
        LETTERS,
        NUMBERS,
        PUNCTUATION,
        EDITING,
        FUNCTIONS,
        NAVIGATION,
        MODIFIERS
    }
}