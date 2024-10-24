package com.hezae.apam.datas

import com.hezae.apam.R

sealed class Style(val title: Int,val description:Int) {
    data object MICA : Style(R.string.Style_Mica, R.string.Style_Mica)
    data object DARK : Style(R.string.Style_Dark, R.string.Style_Dark)
    data object TWILIGHT : Style(R.string.Style_Twi, R.string.Style_Twi)
}