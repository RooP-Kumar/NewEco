package com.example.presentation.ui.screen.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.core.utils.AuthDataStoreImp
import com.example.core.utils.Utility
import com.example.setting.presentation.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var dataStore : AuthDataStoreImp
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        binding.activity = this
    }

    fun onSignOut() {
        Utility.showLoadingDialog(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.setIsAuth(false)
            delay(300)
            Utility.hideLoadingDialog()
            findNavController().navigate("myapp://com.example/auth_activity".toUri())
            activity?.finish()
        }
    }
}