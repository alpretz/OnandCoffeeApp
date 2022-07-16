package com.tugasakhir.onandcafe.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tugasakhir.onandcafe.base.enable
import com.tugasakhir.onandcafe.base.showToast
import com.tugasakhir.onandcafe.base.visible
import com.tugasakhir.onandcafe.databinding.ActivityLoginBinding
import com.tugasakhir.onandcafe.ui.ViewModelFactory
import com.tugasakhir.onandcafe.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        if (viewModel.isLoggedIn()) moveToHome()

        binding.apply {
            viewModel.getIsLoading().observe(this@LoginActivity) {
                pbLogin.visible(it)
                btnLogin.enable(!it)
            }

            viewModel.getOnError().observe(this@LoginActivity) {
                if (it.isNotEmpty()) showToast(this@LoginActivity, it, Toast.LENGTH_SHORT)
            }

            viewModel.getIsSuccess().observe(this@LoginActivity) {
                if (it != null) moveToHome()
            }

            btnLogin.setOnClickListener {
                val username = tilUsername.editText?.text.toString()
                val password = tilPassword.editText?.text.toString()

                viewModel.authenticateUser(username, password)
            }
        }
    }

    private fun moveToHome() {
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}