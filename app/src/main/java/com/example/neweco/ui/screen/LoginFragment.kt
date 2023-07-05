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
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neweco.R
import com.example.neweco.databinding.FragmentLoginBinding
import com.example.neweco.ui.utils.Utility
import com.example.neweco.ui.viewmodel.AuthViewmodel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel : AuthViewmodel by viewModels()
    private lateinit var binding : FragmentLoginBinding
    private var passwordVisible : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        mainFunc()
        return binding.root
    }

    private fun mainFunc() {
        showLoading()
        emailValidation()

        binding.passwordEndIcon.setOnClickListener { onClickPassEndIcon() }

        binding.emailEndIcon.setOnClickListener { binding.emailField.setText("") }

        binding.gotoRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signUpFragment) }
        binding.loginBtn.setOnClickListener { loginUser() }
    }

    private fun showLoading() {
        val runnable = Runnable {
            findNavController().navigate(R.id.action_loginFragment_to_main_nav_graph)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            it?.let { status ->
                if(status.showLoading) {
                    Utility.showLoadingDialog(requireActivity(), resources)
                } else {
                    Utility.hideLoadingDialog()
                    if(status.result == true) {
                        Snackbar.make(binding.emailEditText, status.message, Snackbar.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed(runnable, 300L)
                    } else {
                        Snackbar.make(binding.emailEditText, status.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loginUser() {
        val email = binding.emailField.text.toString().trim()
        val pass = binding.passwordField.text.toString().trim()
        when{
            email.isEmpty() -> {
                Snackbar.make(binding.emailField, "Enter Email", Snackbar.LENGTH_SHORT).show()
                binding.emailField.requestFocus()
            }
            pass.isEmpty() -> {
                Snackbar.make(binding.passwordField, "Enter Password", Snackbar.LENGTH_SHORT).show()
                binding.passwordField.requestFocus()
            }
            else -> {
                viewModel.loginUser(email,pass)
            }
        }

    }

    private fun onClickPassEndIcon() {
        val cursorPosition = binding.passwordField.selectionStart
        if(passwordVisible) {
            binding.passwordField.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.passwordEndIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.eye_slash, null))
            passwordVisible = false
        } else {
            binding.passwordField.transformationMethod =
                SingleLineTransformationMethod.getInstance()
            binding.passwordEndIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.eye, null))
            passwordVisible = true
        }
        binding.passwordField.setSelection(cursorPosition)
    }

    private fun emailValidation() {
        val pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")

        binding.emailField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let {text ->
                    if (text.isNotEmpty()) {
                        binding.emailEndIcon.visibility = View.VISIBLE
                        val matcher = pattern.matcher(value)
                        if(!matcher.find()) {
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


}