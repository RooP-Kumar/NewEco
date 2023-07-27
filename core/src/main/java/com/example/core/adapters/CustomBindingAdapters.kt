package com.example.core.adapters


import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.core.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:setEndIconDrawable")
fun setEndIconDrawable(textInputLayout: TextInputLayout, drawable: Boolean) {
    if (drawable){
        textInputLayout.endIconDrawable =
            ContextCompat.getDrawable(textInputLayout.context, R.drawable.password_eye_selector)
    }
}
