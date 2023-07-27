package com.example.neweco.ui.screen.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.core.network.data.getLoginData
import com.example.core.utils.AuthDataStoreImp
import com.example.core.utils.Utility
import com.example.neweco.BottomNavigationActivity
import com.example.neweco.R
import com.example.neweco.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject
import com.example.core.R as coreR

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val viewModel : LoginViewModel by viewModels()
    private lateinit var binding : FragmentLoginBinding
    @Inject
    lateinit var authDataStore: AuthDataStoreImp
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

        binding.data = getLoginData(requireContext())

        binding.gotoRegister.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signUpFragment) }
        binding.loginBtn.commonBtn.setOnClickListener { loginUser() }
    }

    private fun showLoading() {
        var isBottomNavigationActivityStated = false
        val runnable = Runnable {
            isBottomNavigationActivityStated = true
            startActivity(Intent(activity, BottomNavigationActivity::class.java))
            activity?.finish()
        }

        viewModel.status.observe(viewLifecycleOwner) {
            it?.let { status ->
                if(status.showLoading) {
                    Utility.showLoadingDialog(requireActivity())
                } else {
                    Utility.hideLoadingDialog()
                    if(status.result == true) {
                        Snackbar.make(binding.passwordEditText.editText, status.message, Snackbar.LENGTH_SHORT).show()
                        CoroutineScope(Dispatchers.Main).launch {
                            authDataStore.setIsAuth(true)
                        }
                        if(!isBottomNavigationActivityStated) {
                            Handler(Looper.getMainLooper()).postDelayed(runnable, 300L)
                        }
                    } else {
                        Snackbar.make(binding.passwordEditText.editText, status.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loginUser() {
        val email = binding.emailEditText.editText.text.toString().trim()
        val pass = binding.passwordEditText.editText.text.toString().trim()
        when{
            email.isEmpty() -> {
                binding.emailEditText.editeTextlayout.helperText = getString(R.string.email_is_required)
                binding.emailEditText.editText.requestFocus()
            }
            pass.isEmpty() -> {
                binding.passwordEditText.editeTextlayout.helperText = getString(R.string.password_is_required)
                binding.passwordEditText.editeTextlayout.setHelperTextColor(ContextCompat.getColorStateList(requireContext(), coreR.color.mtrl_helper_text))
                binding.passwordEditText.editText.requestFocus()
            }
            else -> {
                binding.emailEditText.editeTextlayout.isHelperTextEnabled = false
                binding.passwordEditText.editeTextlayout.isHelperTextEnabled = false
                viewModel.loginUser(email,pass)
            }
        }

    }

    private fun emailValidation() {
        val pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")

        binding.emailEditText.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int) {
                value?.let {text ->
                    if (text.isNotEmpty()) {
                        binding.emailEditText.editeTextlayout.isHelperTextEnabled = false
                        binding.passwordEditText.editeTextlayout.isHelperTextEnabled = false
                        val matcher = pattern.matcher(value)
                        if(!matcher.find()) {
                            binding.emailEditText.editeTextlayout.error = getString(com.example.core.R.string.valid_email)
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


}