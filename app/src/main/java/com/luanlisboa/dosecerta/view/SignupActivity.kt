package com.luanlisboa.dosecerta.view

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivitySignupBinding
import com.luanlisboa.dosecerta.repository.UsuarioRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.PickerUtils
import com.luanlisboa.dosecerta.utils.SnackbarUtils


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioRepository = UsuarioRepository(this)

        binding.datePickerNascimento.setOnClickListener {
            PickerUtils.showDatePickerDialog(this, binding.datePickerNascimento)
        }

        val textoOriginal = binding.btnTelaLogin.text.toString()
        val textoMinusculo = textoOriginal.lowercase()
        val textoFormatado = textoMinusculo.replaceFirstChar { it.uppercase() }
        binding.btnTelaLogin.text = textoFormatado

        binding.btnCadastrar.setOnClickListener {

            val seuNome = binding.editNome.text.toString()
            val dataNascimento = binding.datePickerNascimento.text.toString()
            val seuGenero = binding.editGenero.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when {
                seuNome.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira o seu nome!")
                }
                dataNascimento.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira a sua data de nascimento!")
                }
                seuGenero.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira o seu gênero!")
                }
                email.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira o seu email!")
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    SnackbarUtils.mensagem(it, "Insira um email válido!")
                }
                senha.isEmpty() -> {
                    SnackbarUtils.mensagem(it, "Insira a sua senha")
                }
                senha.length <= 7 -> {
                    SnackbarUtils.mensagem(it, "A senha precisa ter pelo menos 8 caracteres")
                }
                else -> {
                    // Cadastrar o usuário no banco de dados
                    val resultado = usuarioRepository.inserirUsuario(
                        seuNome,
                        dataNascimento,
                        seuGenero,
                        email,
                        senha
                    )
                    if (resultado > 0) {
                        SnackbarUtils.mensagem(it, "Usuário cadastrado com sucesso!")
                        RouterManager.direcionarParaLogin(this)
                    } else {
                        SnackbarUtils.mensagem(it, "Erro ao cadastrar usuário.")
                    }
                }
            }
        }

        binding.btnTelaLogin.setOnClickListener {
            RouterManager.direcionarParaLogin(this)
        }
    }
}
