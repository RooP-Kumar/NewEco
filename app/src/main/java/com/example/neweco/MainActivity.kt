package com.example.neweco

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.core.utils.AuthDataStoreImp
import com.example.core.utils.Utility
import com.example.core.viewmodels.ProductViewModel
import com.example.neweco.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    @Inject
    lateinit var authDataStore : AuthDataStoreImp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Main Ui Section
        mainUi()
    }

    private fun mainUi() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Toast.makeText(this@MainActivity, "Available!", Toast.LENGTH_SHORT).show()
            }

            override fun onLost(network: Network) {
                Toast.makeText(this@MainActivity, "Un-Available!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        Utility.showLoadingDialog(this)
        CoroutineScope(Dispatchers.Main).launch {
            authDataStore.isAuth().collectLatest {
                if(it) {
                    delay(300)
                    Utility.hideLoadingDialog()
                    startActivity(Intent(this@MainActivity, BottomNavigationActivity::class.java))
                    finish()
                } else {
                    Utility.hideLoadingDialog()
                }
            }
        }
        super.onStart()
    }

}