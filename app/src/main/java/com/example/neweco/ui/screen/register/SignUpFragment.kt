package com.example.neweco.ui.screen.register

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
import com.example.core.network.data.getRegisterData
import com.example.core.utils.Utility
import com.example.neweco.R
import com.example.neweco.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import com.example.core.R as coreR

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private val viewModel : RegisterViewModel by viewModels()
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

        binding.data = getRegisterData(requireContext())
        binding.btnText = getString(com.example.core.R.string.register)

        binding.gotoLogin.setOnClickListener { findNavController().navigate(R.id.action_signUpFragment_to_loginFragment) }
        binding.registerBtn.commonBtn.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val email = binding.emailEditText.editText.text.toString().trim()
        val pass = binding.passwordEditText.editText.text.toString().trim()
        val confPass = binding.confPasswordEditText.editText.text.toString().trim()
        val name = binding.nameEditText.editText.text.toString().trim()
        when{
            email.isEmpty() -> {
                Snackbar.make(binding.emailEditText.editText, "Enter Email", Snackbar.LENGTH_SHORT).show()
                binding.emailEditText.editText.requestFocus()
            }
            name.isEmpty() -> {
                Snackbar.make(binding.nameEditText.editText, "Enter Name", Snackbar.LENGTH_SHORT).show()
                binding.nameEditText.editText.requestFocus()
            }
            pass.isEmpty() -> {
                Snackbar.make(binding.passwordEditText.editText, "Enter Password", Snackbar.LENGTH_SHORT).show()
                binding.passwordEditText.editText.requestFocus()
            }
            confPass.isEmpty() -> {
                Snackbar.make(binding.confPasswordEditText.editText, "Enter Confirm Password", Snackbar.LENGTH_SHORT).show()
                binding.confPasswordEditText.editText.requestFocus()
            }
            else -> {
                viewModel.registerUser(email, pass, name, null)
            }
        }
    }

    private fun showLoading() {
        val runnable = Runnable {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            it?.let { status ->
                if(status.showLoading) {
                    Utility.showLoadingDialog(requireActivity())
                } else {
                    Utility.hideLoadingDialog()
                    if(status.result == true) {
                        Snackbar.make(binding.emailEditText.editeTextlayout, status.message, Snackbar.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000L)
                    } else {
                        Snackbar.make(binding.emailEditText.editeTextlayout, status.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onClickPassEndIcon(field : EditText, icon : ImageView, isVisible : Boolean, after : (Boolean) -> Unit) {
        val cursorPosition = field.selectionStart
        if(isVisible) {
            field.transformationMethod =
                PasswordTransformationMethod.getInstance()
            icon.setImageDrawable(ResourcesCompat.getDrawable(resources, coreR.drawable.eye_slash, null))
            after(false)
        } else {
            field.transformationMethod =
                SingleLineTransformationMethod.getInstance()
            icon.setImageDrawable(ResourcesCompat.getDrawable(resources, coreR.drawable.eye, null))
            after(true)
        }
        field.setSelection(cursorPosition)
    }

    private fun validateEmail() {
        val pattern =
            Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")

        binding.emailEditText.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let { text ->
                    if (text.isNotEmpty()) {
                        val matcher = pattern.matcher(value)
                        if (!matcher.find()) {
                            binding.emailEditText.editeTextlayout.error = getString(coreR.string.valid_email)
                        } else {
                            binding.emailEditText.editeTextlayout.isErrorEnabled = false
                        }
                    } else {
                        binding.emailEditText.editeTextlayout.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun validatePass() {
        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&\\-+=])(?=\\S+$).{8,20}$")
        binding.passwordEditText.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let { text ->
                    if (text.isNotEmpty()) {
                        val matcher = pattern.matcher(text)
                        if (!matcher.find()) {
                            binding.passwordEditText.editeTextlayout.error = getString(coreR.string.create_pass_string)
                        } else {
                            binding.passwordEditText.editeTextlayout.isErrorEnabled = false
                        }
                    } else {
                        binding.passwordEditText.editeTextlayout.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun validateConfirmPass() {
        binding.confPasswordEditText.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let {
                    if(it.isNotEmpty()) {
                        val pass = binding.passwordEditText.editText.text.toString()
                        if (pass == it.toString()) {
                            binding.emailEditText.editeTextlayout.isErrorEnabled = false
                        } else {
                            binding.confPasswordEditText.editeTextlayout.error = getString(coreR.string.valid_confPass)
                        }
                    } else {
                        binding.emailEditText.editeTextlayout.isErrorEnabled = false
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }


}