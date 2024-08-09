package com.luanlisboa.dosecerta.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.databinding.ActivityLoginBinding
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class LoginActivity : AppCompatActivity() {

    private val bancoDados by lazy {
        DatabaseHelper(this)
    }

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView( binding.root )

        // Banco de Dados (Não finalizado)

        with(binding){

            btnCadastrar.setOnClickListener{
                salvar()
            }
        }

        val dbHelper = DatabaseHelper(this)

        fun salvar(){

            val titulo = binding.editNome.text.toString()

            try {
                bancoDados.writableDatabase.execSQL(
                    "INSERT INTO tbl_Usuario (login, senha, nome, email, dataNascimento, genero) VALUES"
                )
            }catch (e: Exception){
            }


        }

        //


        supportActionBar?.hide()

        binding.btnEntrar.setOnClickListener{

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when{
                email.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira o seu email!")
                }senha.isEmpty() -> {
                    SnackbarUtils.mensagem(it,"Insira a sua senha!")
                }senha.length <= 7 -> {
                    SnackbarUtils.mensagem(it,"A senha precisa ter pelo menos 8 caracteres")
                }else -> {
                    RouterManager.direcionarParaHome(this)
                }
            }
        }

        binding.btnTelaCadastro.setOnClickListener{
            RouterManager.direcionarParaCadastro(this)
        }
    }



}
