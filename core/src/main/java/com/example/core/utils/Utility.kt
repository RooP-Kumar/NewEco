package com.example.core.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import androidx.core.content.ContextCompat
import com.example.core.R

object Utility {
    private var loadingDialog: Dialog? = null
    private var intent : Intent? = null
    fun showLoadingDialog(
        context: Context,
    ) {
        loadingDialog = Dialog(context)
        loadingDialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(context, R.drawable.dialog_background)
        )
        loadingDialog!!.setCancelable(false)
        loadingDialog!!.setContentView(R.layout.loading_dialog)
        loadingDialog!!.show()
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

}