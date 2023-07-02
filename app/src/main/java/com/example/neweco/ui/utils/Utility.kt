package com.example.neweco.ui.utils

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.Window
import androidx.core.content.res.ResourcesCompat
import com.example.neweco.R

object Utility {
    private var loadingDialog: Dialog? = null
    fun showLoadingDialog(
        context : Context,
        resources : Resources
    ) {
        loadingDialog = Dialog(context)
        loadingDialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.window?.setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.drawable.dialog_background, null))
        loadingDialog!!.setCancelable(false)
        loadingDialog!!.setContentView(R.layout.loading_dialog)
        loadingDialog!!.show()
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }
}