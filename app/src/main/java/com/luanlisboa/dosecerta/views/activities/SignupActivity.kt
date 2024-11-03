package com.luanlisboa.dosecerta.views.activities

import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivitySignupBinding
import com.luanlisboa.dosecerta.repositories.UsuarioRepository
import com.luanlisboa.dosecerta.utils.RouterManager
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

        // Configurando o Spinner para o campo de gênero
        val generos = resources.getStringArray(com.luanlisboa.dosecerta.R.array.genero_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenero.adapter = adapter

        // Formatando texto do botão de login
        val textoOriginal = binding.btnTelaLogin.text.toString()
        val textoMinusculo = textoOriginal.lowercase()
        val textoFormatado = textoMinusculo.replaceFirstChar { it.uppercase() }
        binding.btnTelaLogin.text = textoFormatado

        binding.btnCadastrar.setOnClickListener {

            var seuNome = binding.editNome.text.toString()
            var dataNascimento = binding.datePickerNascimento.text.toString()
            var seuGenero = binding.spinnerGenero.selectedItem.toString()
            var email = binding.editEmail.text.toString()
            var senha = binding.editSenha.text.toString()

            when {
                seuNome.isEmpty() -> {
                    Toast.makeText(this, "Insira o seu nome!", Toast.LENGTH_SHORT).show()
                }
                dataNascimento.isEmpty() -> {
                    Toast.makeText(this, "Insira a sua data de nascimento!", Toast.LENGTH_SHORT).show()
                }
                seuGenero == "Selecione o gênero" -> {
                    Toast.makeText(this, "Selecione o seu gênero!", Toast.LENGTH_SHORT).show()
                }
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
                    // Cadastrar o usuário no banco de dados
                    val resultado = usuarioRepository.inserirUsuario(
                        seuNome,
                        dataNascimento,
                        seuGenero,
                        email,
                        senha
                    )
                    if (resultado > 0) {
                        Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        RouterManager.direcionarParaLogin(this)
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnTelaLogin.setOnClickListener {
            RouterManager.direcionarParaLogin(this)
        }
    }
}