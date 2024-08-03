package com.luanlisboa.dosecerta

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.luanlisboa.dosecerta.databinding.ActivityMainBinding
import com.luanlisboa.dosecerta.view.Cadastro
import com.luanlisboa.dosecerta.view.Home

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener{

            val nome = binding.editNome.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                nome.isEmpty() -> {
                    mensagem(it,"Insira o seu email!")
                }senha.isEmpty() -> {
                    mensagem(it,"Insira a sua senha!")
                }senha.length <= 7 -> {
                    mensagem(it,"A senha precisa ter pelo menos 8 caracteres")
                }else -> {
                    DirecionarParaHome()
                }
            }
        }

    }

    private fun mensagem(view: View, mensagem: String){
        val snackbar = Snackbar.make(view,mensagem,Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#1F2B5B"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun DirecionarParaHome(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

}