package com.example.core.network.data

import android.content.Context
import android.text.InputType
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputLayout

data class DataBinding_textField(
    val hint : String,
    val endIconMode : Int,
    val startIcon : Int,
    val endIcon : Boolean,
    val inputType : Int,
    val imeOption : Int
)


fun getLoginData(context: Context) = listOf(
    DataBinding_textField(
        hint = context.getString(com.example.core.R.string.email),
        endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT,
        startIcon = com.example.core.R.drawable.baseline_email_24,
        endIcon = false,
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        imeOption = EditorInfo.IME_ACTION_NEXT
    ),
    DataBinding_textField(
        hint =  context.getString(com.example.core.R.string.password),
        endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE,
        startIcon = com.example.core.R.drawable.baseline_lock_24,
        endIcon = true,
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
        imeOption = EditorInfo.IME_ACTION_DONE
    )
)

fun getRegisterData(context: Context) = listOf(
    DataBinding_textField(
        hint = context.getString(com.example.core.R.string.name),
        endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT,
        startIcon = com.example.core.R.drawable.baseline_person_24,
        endIcon = false,
        inputType = InputType.TYPE_CLASS_TEXT,
        imeOption = EditorInfo.IME_ACTION_NEXT
    ),
    DataBinding_textField(
        hint =  context.getString(com.example.core.R.string.email),
        endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT,
        startIcon = com.example.core.R.drawable.baseline_email_24,
        endIcon = false,
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        imeOption = EditorInfo.IME_ACTION_NEXT
    ),DataBinding_textField(
        hint =  context.getString(com.example.core.R.string.confirm_password),
        endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE,
        startIcon = com.example.core.R.drawable.baseline_lock_24,
        endIcon = true,
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
        imeOption = EditorInfo.IME_ACTION_DONE
    ),
    DataBinding_textField(
        hint =  context.getString(com.example.core.R.string.confirm_password),
        endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE,
        startIcon = com.example.core.R.drawable.baseline_lock_24,
        endIcon = true,
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
        imeOption = EditorInfo.IME_ACTION_DONE
    )
)