package com.example.demonstrationapp.utils

import android.view.View
import android.widget.AdapterView

abstract class OnItemSelectedListenerAdapter() : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}