package com.luanlisboa.dosecerta.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(

    // 1 Contexto -- 2 Nome do BD -- 3 Cursor -- 4 Versão do DB

    context, "BancoDados.db", null, 1

) {

    override fun onCreate(db: SQLiteDatabase?) {

        Log.i("info_db", "Executou onCreate")

        // Tabela de Anotação

        val sqlAnotacao = "CREATE TABLE tbl_Anotacao ("  +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT," +
                "mensagem TEXT NOT NULL,"  +
                "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");"
        try {

            db?.execSQL(sqlAnotacao)
            Log.i("info_db", "Sucesso ao criar a tabela")

        }catch (e: Exception){
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar a tabela")
        }

        // Tabela tbl_Endereco

        val sqlEndereco = "CREATE TABLE tbl_Endereco ("  +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cep TEXT NOT NULL,"  +
                "estado TEXT NOT NULL," +
                "cidade TEXT NOT NULL," +
                "bairro TEXT NOT NULL," +
                "rua TEXT NOT NULL," +
                "numero TEXT NOT NULL," +
                "complemento TEXT," +
                "id_usuario INTEGER," +
                "FOREIGN KEY (id_usuario) REFERENCES tbl_Usuario(id)" +
                ");"
        try {
            db?.execSQL(sqlEndereco)
            Log.i("info_db", "Sucesso ao criar a tabela tbl_Endereco")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar a tabela tbl_Endereco")
        }

        // Tabela tbl_Usuario

        val sqlUsuario = "CREATE TABLE tbl_Usuario ("  +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senha TEXT NOT NULL," +
                "nome TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "dataNascimento DATE NOT NULL," +
                "genero TEXT NOT NULL" +
                ");"
        try {
            db?.execSQL(sqlUsuario)
            Log.i("info_db", "Sucesso ao criar a tabela tbl_Usuario")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar a tabela tbl_Usuario")
        }


        // Tabela tbl_Medicamento

        val sqlMedicamento = "CREATE TABLE tbl_Medicamento ("  +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL,"  +
                "formato TEXT NOT NULL," +
                "medida TEXT NOT NULL," +
                "unidMedida TEXT NOT NULL," +
                "quantEstoque INTEGER," +
                "formatoEstoque TEXT," +
                "id_usuario INTEGER NOT NULL," +
                "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "data_atualizacao DATETIME" +
                ");"
        try {
            db?.execSQL(sqlMedicamento)
            Log.i("info_db", "Sucesso ao criar a tabela tbl_Medicamento")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar a tabela tbl_Medicamento")
        }


        // Tabela tbl_Alerta

        val sqlAlerta = "CREATE TABLE tbl_Alerta ("  +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "periodicidade TEXT NOT NULL,"  +
                "horarioPrimeiraDose TIME NOT NULL," +
                "diasTratamento TEXT NOT NULL," +
                "dosagem TEXT NOT NULL," +
                "notificar INTEGER NOT NULL," +
                "id_usuario INTEGER," +
                "id_medicamento INTEGER," +
                "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (id_usuario) REFERENCES tbl_Usuario(id)," +
                "FOREIGN KEY (id_medicamento) REFERENCES tbl_Medicamento(id)" +
                ");"
        try {
            db?.execSQL(sqlAlerta)
            Log.i("info_db", "Sucesso ao criar a tabela tbl_Alerta")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar a tabela tbl_Alerta")
        }

         // Tabela tbl_Agenda

        val sqlAgenda = "CREATE TABLE tbl_Agenda ("  +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dataHora DATETIME NOT NULL,"  +
                "situacaoIngestao INTEGER NOT NULL," +
                "id_alerta INTEGER," +
                "id_anotacao INTEGER," +
                "FOREIGN KEY (id_alerta) REFERENCES tbl_Alerta(id)," +
                "FOREIGN KEY (id_anotacao) REFERENCES tbl_Anotacao(id)" +
                ");"
        try {
            db?.execSQL(sqlAgenda)
            Log.i("info_db", "Sucesso ao criar a tabela tbl_Agenda")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao criar a tabela tbl_Agenda")
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        Log.i("info_db", "Executou onUpgrade")

    }
}
