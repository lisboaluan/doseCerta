package com.luanlisboa.dosecerta.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.databinding.ActivitySignupBinding
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SnackbarUtils
import java.util.Calendar

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.datePickerNascimento.setOnClickListener { showDatePickerDialog() }

        // Faz com que o botão 'entrar' fique com letras minúsculas na tela de cadastro
        val textoOriginal = binding.btnTelaLogin.text.toString()
        val textoMinusculo = textoOriginal.lowercase()
        val textoFormatado = textoMinusculo.replaceFirstChar { it.uppercase() }
        binding.btnTelaLogin.text = textoFormatado

        binding.btnCadastrar.setOnClickListener{

            val seuNome = binding.editNome.text.toString()
            val dataNascimento = binding.datePickerNascimento.text.toString()
            val seuGenero = binding.editGenero.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                seuNome.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira o seu nome!")
                }dataNascimento.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira a sua data de nascimento!")
                }seuGenero.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira o seu gênero!")
                }email.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira o seu email!")
                }senha.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira a sua senha")
                }senha.length <= 7 -> {
                    SnackbarUtils.mensagem(it,"A senha precisa ter pelo menos 8 caracteres")
                }else -> {
                    RouterManager.direcionarParaHome(this)
                    // Incluir talvez uma mensagem informando que o cadastro foi concluído com sucesso
                }
            }
        }

        binding.btnTelaLogin.setOnClickListener{
            RouterManager.direcionarParaLogin(this)
        }
    }

    // Faz com que exiba um calendário no campo 'data de nascimento'
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.datePickerNascimento.setText(date)
            },
            year, month, day
        )

        datePickerDialog.show()
    }
}