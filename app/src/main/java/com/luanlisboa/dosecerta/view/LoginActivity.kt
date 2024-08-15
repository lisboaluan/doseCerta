package com.luanlisboa.dosecerta.view

import android.database.sqlite.SQLiteStatement
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.databinding.ActivityLoginBinding
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SnackbarUtils

class LoginActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val bancoDados by lazy {
        DatabaseHelper(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

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


        /*

        with(binding){

            btnCadastrar.setOnClickListener{
                salvar()
            }

            btnCadastraTratamento.setOnClickListener {
                listar()
            }
        }

        val dbHelper = DatabaseHelper(this)

         // Função para salvar um medicamento no banco de dados

        fun salvarMedicamento(){

            val nome = binding.editNomeMedicamento.text.toString()
            val formato = binding.editFormatoMedicamento.text.toString()
            val medida = binding.editMedidaMedicamento.text.toString()
            val unidMedida = binding.editUnidMedida.text.toString()
            val quantEstoque = binding.editQuantEstoque.text.toString()
            val formatoEstoque = binding.editFormatoEstoque.text.toString()

            if (nome.isEmpty() || formato.isEmpty() || medida.isEmpty() || unidMedida.isEmpty() || quantEstoque.isEmpty() || formatoEstoque.isEmpty()) {
                SnackbarUtils.mensagem(binding.root, "Todos os campos são obrigatórios!")
                return
            }

            val sql = "INSERT INTO tbl_Medicamento (nome, formato, medida, unidMedida, quantEstoque, formatoEstoque, data_criacao) VALUES (?, ?, ?, ?, ?, ?, datetime('now'));"

            try {
                val stmt: SQLiteStatement = bancoDados.writableDatabase.compileStatement(sql)
                stmt.bindString(1, nome)
                stmt.bindString(2, formato)
                stmt.bindString(3, medida)
                stmt.bindString(4, unidMedida)
                stmt.bindString(5, quantEstoque)
                stmt.bindString(6, formatoEstoque)

                stmt.executeInsert()
                Log.i("info_db", "Medicamento inserido: Nome: $nome, Formato: $formato, Medida: $medida, Unidade de Medida: $unidMedida, Quantidade em Estoque: $quantEstoque, Formato Estoque: $formatoEstoque")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("info_db", "Erro ao inserir medicamento")
            }
        }

        // Função para listar medicamentos do banco de dados

        fun listarMedicamentos() {

            val sql = "SELECT * FROM tbl_Medicamento;"
            val cursor = bancoDados.readableDatabase.rawQuery(sql, null)

            val indiceId = cursor.getColumnIndex("id")
            val indiceNome = cursor.getColumnIndex("nome")
            val indiceFormato = cursor.getColumnIndex("formato")
            val indiceMedida = cursor.getColumnIndex("medida")
            val indiceUnidMedida = cursor.getColumnIndex("unidMedida")
            val indiceQuantEstoque = cursor.getColumnIndex("quantEstoque")
            val indiceFormatoEstoque = cursor.getColumnIndex("formatoEstoque")
            val indiceDataCriacao = cursor.getColumnIndex("data_criacao")

            while (cursor.moveToNext()) {
                val idMedicamento = cursor.getInt(indiceId)
                val nome = cursor.getString(indiceNome)
                val formato = cursor.getString(indiceFormato)
                val medida = cursor.getString(indiceMedida)
                val unidMedida = cursor.getString(indiceUnidMedida)
                val quantEstoque = cursor.getString(indiceQuantEstoque)
                val formatoEstoque = cursor.getString(indiceFormatoEstoque)
                val dataCriacao = cursor.getString(indiceDataCriacao)

                // Log com medicamentos listados
                Log.i("info_db", "ID: $idMedicamento - Nome: $nome - Formato: $formato - Medida: $medida - Unidade de Medida: $unidMedida - Quantidade em Estoque: $quantEstoque - Formato Estoque: $formatoEstoque - Data de Criação: $dataCriacao")
            }
            cursor.close()
        }




         */

 }
}
