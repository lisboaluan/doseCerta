package com.luanlisboa.dosecerta.views.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityLoginBinding
import com.luanlisboa.dosecerta.repositories.UsuarioRepository
import com.luanlisboa.dosecerta.utils.RouterManager
import com.luanlisboa.dosecerta.utils.SessionManager
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

        // Inicializa a interface de usuário
        setContentView(binding.root)
        supportActionBar?.hide()

        // Verifica se há um usuário salvo nas SharedPreferences para login automático
        val sharedPref: SharedPreferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE)
        val savedUserId = sharedPref.getInt("idUsuario", -1)  // Se não houver ID salvo, retorna -1

        if (savedUserId > 0) {
            // Verifica se o usuário com o ID salvo ainda existe no banco de dados
            val usuarioExiste = usuarioRepository.buscarIdUsuario(savedUserId)

            if (usuarioExiste != null) {
                // Se o usuário ainda existir, realiza login automático
                SessionManager.loggedInUserId = savedUserId
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                return
            }
        }

        setupLogin()
    }

    /**
     * Configura os eventos de login, como clique no botão "Entrar" e redirecionamento para cadastro.
     */
    private fun setupLogin() {
        binding.btnEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            val manterConectado = binding.checkboxManterConectado.isChecked

            when {
                email.isEmpty() -> {
                    Toast.makeText(this, "Insira o seu email!", Toast.LENGTH_SHORT).show()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Insira um email válido!", Toast.LENGTH_SHORT).show()
                }
                senha.isEmpty() -> {
                    Toast.makeText(this, "Insira a sua senha!", Toast.LENGTH_SHORT).show()
                }
                senha.length <= 7 -> {
                    Toast.makeText(this, "A senha precisa ter pelo menos 8 caracteres", Toast.LENGTH_SHORT).show()
                }


                else -> {
                    // Verifica as credenciais no banco de dados
                    val idUsuario = usuarioRepository.validarUsuario(email, senha)
                    if (idUsuario != null && idUsuario > 0) {
                        if (manterConectado) {
                            // Se o checkbox "manter conectado" estiver marcado, salva o ID nas SharedPreferences
                            saveUserId(this, idUsuario)
                        }
                        SessionManager.loggedInUserId = idUsuario
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Email ou senha incorretos.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnTelaCadastro.setOnClickListener {
            RouterManager.direcionarParaCadastro(this)
        }
    }

    /**
     * Salva o ID do usuário nas SharedPreferences para login automático futuro.
     */
    private fun saveUserId(context: Context, userId: Int) {
        val sharedPref: SharedPreferences = context.getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("idUsuario", userId)
            apply()
        }
    }
}
