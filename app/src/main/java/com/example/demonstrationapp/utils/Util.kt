package com.example.demonstrationapp.utils

import android.widget.ImageView
import com.example.demonstrationapp.R

fun ImageView.setCheckedStar(isChecked: Boolean) {
    val icon = when (isChecked) {
        false -> R.drawable.ic_star_white
        true -> R.drawable.ic_star_blue
    }
    setImageResource(icon)
}