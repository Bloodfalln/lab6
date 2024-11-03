package com.example.lab6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkReceiver: NetworkReceiver
    private var listenNetworkStatus: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ініціалізація binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ініціалізація BroadcastReceiver для прослуховування стану мережі
        networkReceiver = NetworkReceiver()

        // Додаємо прослуховувач для `CheckBox`, щоб вмикати/вимикати прослуховування мережі
        binding.networkCheckBox.setOnCheckedChangeListener { _, isChecked ->
            listenNetworkStatus = isChecked
            if (listenNetworkStatus) {
                registerNetworkReceiver()
            } else {
                unregisterReceiver(networkReceiver)
            }
        }

        // Реєстрація BroadcastReceiver
        if (listenNetworkStatus) {
            registerNetworkReceiver()
        }
    }

    private fun registerNetworkReceiver() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (listenNetworkStatus) {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                if (activeNetwork?.isConnected == true) {
                    showToast(context, "Підключено до мережі \uD83D\uDCBE")
                } else {
                    showToast(context, "Відключено від мережі \uD83D\uDCF6")
                }
            }
        }

        private fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}