package com.raystatic.iconfinder.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.raystatic.iconfinder.R

object ViewExtension {

    fun View.showSnack(message:String){
        Snackbar.make(this,message, Snackbar.LENGTH_SHORT).show()
    }

    fun View.showToast(context:Context,message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun View.showCustomSnack(context: Context, msg:String){
        val snackbar = Snackbar.make(this,msg,Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))
        snackbar.show()
    }

}