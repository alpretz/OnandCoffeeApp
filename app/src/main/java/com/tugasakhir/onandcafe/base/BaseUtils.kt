package com.tugasakhir.onandcafe.base

import android.content.Context
import android.view.View
import android.widget.Toast
import com.tugasakhir.onandcafe.R
import java.util.regex.Pattern

fun showToast(context: Context, text: String, duration: Int) {
    Toast.makeText(context, text, duration).show()
}

fun checkIsEmailValid(email: CharSequence): Boolean {
    val ePattern =
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    val p = Pattern.compile(ePattern)
    val m = p.matcher(email)
    return m.matches()
}

fun invalidateAuthForm(
    context: Context,
    username: String,
    email: String,
    password: String
): Boolean {
    return if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
        showToast(context, context.getString(R.string.please_fill), Toast.LENGTH_SHORT)
        false
    } else if (!checkIsEmailValid(email)) {
        showToast(context, context.getString(R.string.enter_valid_email), Toast.LENGTH_SHORT)
        false
    } else if (password.length < 6) {
        showToast(context, context.getString(R.string.password_short), Toast.LENGTH_SHORT)
        false
    } else {
        true
    }
}

fun View.visible(value: Boolean) {
    if (value) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun View.enable(value: Boolean) {
    this.isEnabled = value
}