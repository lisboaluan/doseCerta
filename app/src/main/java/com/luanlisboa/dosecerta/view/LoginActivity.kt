package com.luanlisboa.dosecerta.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivityLoginBinding
import com.luanlisboa.dosecerta.repository.UsuarioRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SessionManager
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class LoginActivity : AppCompatActivity() {

    // Utiliza o ViewBinding para acessar os elementos da interface de forma eficiente
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    // Inicializa o repositório de usuários
    private val usuarioRepository by lazy {
        UsuarioRepository(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa a interface de usuário
        setContentView(binding.root)
        supportActionBar?.hide()  // Esconde a barra de ação

        // Recupera o ID do usuário salvo nas SharedPreferences
        val sharedPref: SharedPreferences = getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE)
        val savedUserId = sharedPref.getInt("idUsuario", -1)  // Se não existir ID salvo, retorna -1

        if (savedUserId > 0) {
            // Verifica se o ID ainda está presente no banco de dados
            val usuarioExiste = usuarioRepository.buscarUsuarioPorId(savedUserId)

            if (usuarioExiste != null) {
                // Se o usuário ainda existir no banco, realiza o login automático
                SessionManager.loggedInUserId = savedUserId
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()   // Finaliza a LoginActivity após o login automático
                return     // Evita a execução do restante do código
            }
        }

        // Se o ID não estiver salvo ou o usuário não existir mais, prossegue com o fluxo normal de login
        setupLogin()
    }

    // Função que configura os eventos de login, como o clique no botão de entrar
    private fun setupLogin() {
        binding.btnEntrar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            val manterConectado = binding.checkboxManterConectado.isChecked  // Verifica se o checkbox de "manter conectado" está marcado

            when {
                email.isEmpty() -> {
                    // Exibe uma mensagem caso o campo de email esteja vazio
                    SnackbarUtils.mensagem(it, "Insira o seu email!")
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    // Exibe uma mensagem caso o email inserido seja inválido
                    SnackbarUtils.mensagem(it, "Insira um email válido!")
                }

                senha.isEmpty() -> {
                    // Exibe uma mensagem caso o campo de senha esteja vazio
                    SnackbarUtils.mensagem(it, "Insira a sua senha!")
                }

                senha.length <= 7 -> {
                    // Exibe uma mensagem caso a senha seja muito curta
                    SnackbarUtils.mensagem(it, "A senha precisa ter pelo menos 8 caracteres")
                }

                else -> {
                    // Valida as credenciais do usuário no banco de dados
                    val idUsuario = usuarioRepository.validarUsuario(email, senha)
                    if (idUsuario != null && idUsuario > 0) {
                        if (manterConectado && email.isNotEmpty() && senha.isNotEmpty()) {
                            // Se o checkbox "manter conectado" estiver marcado, salva o ID do usuário nas SharedPreferences
                            saveUserId(this, idUsuario)
                        }
                        // Define o ID do usuário logado na sessão e redireciona para a HomeActivity
                        SessionManager.loggedInUserId = idUsuario
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()  // Finaliza a LoginActivity
                    } else {
                        // Exibe uma mensagem caso as credenciais estejam incorretas
                        SnackbarUtils.mensagem(it, "Email ou senha incorretos.")
                    }
                }
            }
        }

        // Redireciona para a tela de cadastro caso o botão "Cadastre-se" seja clicado
        binding.btnTelaCadastro.setOnClickListener {
            RouterManager.direcionarParaCadastro(this)
        }
    }

    // Função que salva o ID do usuário nas SharedPreferences para manter a sessão
    private fun saveUserId(context: Context, userId: Int) {
        val sharedPref: SharedPreferences = context.getSharedPreferences("UsuarioPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("idUsuario", userId)  // Salva o ID do usuário
            apply()  // Aplica a mudança de forma assíncrona
        }
    }
}