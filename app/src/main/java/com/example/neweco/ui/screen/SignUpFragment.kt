package com.example.neweco.ui.screen

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neweco.R
import com.example.neweco.databinding.FragmentSignUpBinding
import com.example.neweco.ui.utils.Utility
import com.example.neweco.ui.viewmodel.AuthViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private var passwordVisible : Boolean = false
    private var confPasswordVisible : Boolean = false
    private val viewModel : AuthViewmodel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater)
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        showLoading()
        validateEmail()
        validatePass()
        validateConfirmPass()

        binding.passwordEndIcon.setOnClickListener { onClickPassEndIcon(binding.passwordField, binding.passwordEndIcon, passwordVisible){passwordVisible = it} }
        binding.confPasswordEndIcon.setOnClickListener { onClickPassEndIcon(binding.confPasswordField, binding.confPasswordEndIcon, confPasswordVisible){confPasswordVisible = it} }

        binding.emailEndIcon.setOnClickListener { binding.emailField.setText("") }

        binding.gotoLogin.setOnClickListener { findNavController().navigate(R.id.action_signUpFragment_to_loginFragment) }

        binding.registerBtn.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val email = binding.emailField.text.toString().trim()
        val pass = binding.passwordField.text.toString().trim()
        viewModel.registerUser(email, pass)
    }

    private fun showLoading() {
        val runnable = Runnable {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            it?.let { status ->
                if(status.showLoading) {
                    Utility.showLoadingDialog(requireActivity(), resources)
                } else {
                    Utility.hideLoadingDialog()
                    if(status.result == true) {
                        Snackbar.make(binding.emailEditText, status.message, Snackbar.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000L)
                    } else {
                        Snackbar.make(binding.emailEditText, status.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onClickPassEndIcon(field : EditText, icon : ImageView, isVisible : Boolean, after : (Boolean) -> Unit) {
        val cursorPosition = field.selectionStart
        if(isVisible) {
            field.setText(getString(R.string.demo_text))
            field.transformationMethod =
                PasswordTransformationMethod.getInstance()
            icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.eye_slash, null))
            after(false)
        } else {
            field.transformationMethod =
                SingleLineTransformationMethod.getInstance()
            icon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.eye, null))
            after(true)
        }
        field.setSelection(cursorPosition)
    }

    private fun validateEmail() {
        val pattern =
            Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")

        binding.emailField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let { text ->
                    if (text.isNotEmpty()) {
                        binding.emailEndIcon.visibility = View.VISIBLE
                        val matcher = pattern.matcher(value)
                        if (!matcher.find()) {
                            binding.emailMsg.visibility = View.VISIBLE
                            binding.emailMsg.text = getString(R.string.valid_email)
                            binding.emailMsg.setTextColor(Color.rgb(200, 0, 0))
                        } else {
                            binding.emailMsg.visibility = View.INVISIBLE
                        }
                    } else {
                        binding.emailEndIcon.visibility = View.GONE
                        binding.emailMsg.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun validatePass() {
        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&\\-+=])(?=\\S+$).{8,20}$")
        binding.passwordField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let { text ->
                    if (text.isNotEmpty()) {
                        binding.emailEndIcon.visibility = View.VISIBLE
                        val matcher = pattern.matcher(text)
                        if (!matcher.find()) {
                            binding.passMsg.visibility = View.VISIBLE
                            binding.passMsg.text = getString(R.string.create_pass_string)
                            binding.passMsg.setTextColor(Color.rgb(200, 0, 0))
                        } else {
                            binding.passMsg.text = getString(R.string.demo_text)
                            binding.passMsg.visibility = View.INVISIBLE
                        }
                    } else {
                        binding.passMsg.text = getString(R.string.demo_text)
                        binding.emailEndIcon.visibility = View.GONE
                        binding.passMsg.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun validateConfirmPass() {
        binding.confPasswordField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let {
                    if(it.isNotEmpty()) {
                        val pass = binding.passwordField.text
                        if (pass == it) {
                            binding.confPassMsg.visibility = View.INVISIBLE
                        } else {
                            binding.confPassMsg.visibility = View.VISIBLE
                            binding.confPassMsg.text = getString(R.string.valid_confPass)
                            binding.confPassMsg.setTextColor(Color.rgb(200, 0, 0))
                        }
                    } else {
                        binding.confPassMsg.text = getString(R.string.demo_text)
                        binding.confPassMsg.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }


}