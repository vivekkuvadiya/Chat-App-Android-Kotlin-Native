package com.vivek.chatingapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPasswordIsSame(confirmPassword: String) = this == confirmPassword

fun EditText.isFiledIsEmpty() = this.text.toString().trim().isEmpty()

fun EditText.getFiledText() = this.text.toString().trim()

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Bitmap.encodeToBase64(): String {
    val previewWidth = 150
    val previewHeight = this.height * previewWidth / this.width
    val previewBitmap = Bitmap.createScaledBitmap(this, previewWidth, previewHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun String.decodeToBitmap():Bitmap{
    val byte = Base64.decode(this,Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byte,0,byte.size)
}

fun SharedPreferences.putAny(key: String, value: Any) {
    when (value) {
        is String -> edit().putString(key, value).apply()
        is Int -> edit().putInt(key, value).apply()
        is Boolean -> edit().putBoolean(key, value).apply()
    }
}

fun String.encodeBase64():String{
    var data = ByteArray(0)
    try {
        data = this.toByteArray ()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    } finally {
        return Base64.encodeToString(data, Base64.DEFAULT)
    }
}

fun String.decodeBase64():String{
    val dataDec: ByteArray = Base64.decode(this, Base64.DEFAULT)
    var decodedString = ""
    try {
        decodedString = String(dataDec)
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    } finally {
        return decodedString!!
    }

}

fun SharedPreferences.clearAll(){
    edit().clear().apply()
}

fun Date.getReadableDate():String{
    return SimpleDateFormat("MMMM dd, yyyy - hh:mm a",Locale.getDefault()).format(this)
}