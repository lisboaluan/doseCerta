package com.luanlisboa.dosecerta.view

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityLoginBinding
import com.luanlisboa.dosecerta.repository.UsuarioRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val usuarioRepository by lazy {
        UsuarioRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnEntrar.setOnClickListener {

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when {
                email.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira o seu email!")
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    SnackbarUtils.mensagem(it, "Insira um email válido!")
                }
                senha.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira a sua senha!")
                }
                senha.length <= 7 -> {
                    SnackbarUtils.mensagem(it, "A senha precisa ter pelo menos 8 caracteres")
                }
                else -> {
                    // Validar o usuário com o método do UsuarioRepository
                    if (usuarioRepository.validarUsuario(email, senha)) {
                        RouterManager.direcionarParaHome(this)
                    } else {
                        SnackbarUtils.mensagem(it, "Email ou senha incorretos.")
                    }
                }
            }
        }

        binding.btnTelaCadastro.setOnClickListener {
            RouterManager.direcionarParaCadastro(this)
        }
    }
}

