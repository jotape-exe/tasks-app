package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import kotlin.math.log

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Layout
        setContentView(binding.root)

        // Eventos
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)

        viewModel.verifyLoggedUser()

        // Observadores
        observe()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_login -> handleLogin()
        }
    }


    private fun observe() {
        viewModel.login.observe(this) {
            if (it.getSatus()) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(applicationContext, it.getMessage(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loggedUser.observe(this){
            if (it){
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }


    }

    private fun handleLogin() {
        val email: String = binding.editEmail.text.toString()
        val password: String = binding.editPassword.text.toString()
        viewModel.doLogin(email, password)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}