package com.example.presentation.ui.screen.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment() {

    private var productId : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        productId = arguments?.getString("productId")
        return ComposeView(requireContext()).apply {
            setContent {
                MainUI()
            }
        }
    }

    @Composable
    private fun MainUI() {
        LaunchedEffect(key1 = productId, block = {

            if(productId != null) {

            }

        })
    }
}