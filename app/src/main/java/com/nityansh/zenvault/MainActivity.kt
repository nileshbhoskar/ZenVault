package com.nityansh.zenvault

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nityansh.dashboard.viewmodel.DashboardViewModel
import com.nityansh.transaction.viewmodel.TransactionViewModel
import com.nityansh.zenvault.ui.theme.ZenVaultTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.security.KeyStore
import javax.crypto.KeyGenerator

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val dashboardVM: DashboardViewModel by viewModels()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val transactionVM: TransactionViewModel by viewModels()

            ZenVaultTheme {
                MainScreen(dashboardVM)
            }
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_ID", "12345")
        }


    }
}