package com.picpay.desafio.android.util.extensions

import android.app.Activity
import android.widget.Toast
import com.picpay.desafio.android.R

fun Activity.showToast() {
    Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show()
}