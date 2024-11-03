package com.luanlisboa.dosecerta.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "BancoDados.db"
        private const val DATABASE_VERSION = 1

        // Nomes das tabelas
        private const val TABLE_ANOTACAO = "tbl_Anotacao"
        private const val TABLE_ENDERECO = "tbl_Endereco"
        private const val TABLE_USUARIO = "tbl_Usuario"
        private const val TABLE_MEDICAMENTO = "tbl_Medicamento"
        private const val TABLE_ALERTA = "tbl_Alerta"
        private const val TABLE_AGENDA = "tbl_Agenda"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i("info_db", "Executou onCreate")

        val createTables = arrayOf(
            "CREATE TABLE $TABLE_USUARIO (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "senha TEXT NOT NULL," +
                    "nome TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "dataNascimento DATE NOT NULL," +
                    "genero TEXT NOT NULL);",

            "CREATE TABLE $TABLE_ANOTACAO (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT," +
                    "mensagem TEXT NOT NULL," +
                    "id_usuario INTEGER," +
                    "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (id_usuario) REFERENCES $TABLE_USUARIO(id));",

            "CREATE TABLE $TABLE_ENDERECO (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cep TEXT NOT NULL," +
                    "estado TEXT NOT NULL," +
                    "cidade TEXT NOT NULL," +
                    "bairro TEXT NOT NULL," +
                    "rua TEXT NOT NULL," +
                    "numero TEXT NOT NULL," +
                    "complemento TEXT," +
                    "id_usuario INTEGER," +
                    "FOREIGN KEY (id_usuario) REFERENCES $TABLE_USUARIO(id));",

            "CREATE TABLE $TABLE_MEDICAMENTO (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL," +
                    "formato TEXT NOT NULL," +
                    "medida TEXT NOT NULL," +
                    "unidMedida TEXT NOT NULL," +
                    "quantEstoque INTEGER," +
                    "formatoEstoque TEXT," +
                    "id_usuario INTEGER NOT NULL," +
                    "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "data_atualizacao DATETIME," +
                    "FOREIGN KEY (id_usuario) REFERENCES $TABLE_USUARIO(id));",

            "CREATE TABLE $TABLE_ALERTA (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "periodicidade TEXT NOT NULL," +
                    "horarioPrimeiraDose TIME NOT NULL," +
                    "diasTratamento TEXT NOT NULL," +
                    "dosagem TEXT NOT NULL," +
                    "notificar INTEGER NOT NULL," +
                    "id_usuario INTEGER," +
                    "id_medicamento INTEGER," +
                    "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (id_usuario) REFERENCES $TABLE_USUARIO(id)," +
                    "FOREIGN KEY (id_medicamento) REFERENCES $TABLE_MEDICAMENTO(id));",

            "CREATE TABLE $TABLE_AGENDA (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "dataHora DATETIME NOT NULL," +
                    "situacaoIngestao INTEGER NOT NULL," +
                    "id_alerta INTEGER," +
                    "id_medicamento INTEGER," +
                    "id_anotacao INTEGER," +
                    "FOREIGN KEY (id_alerta) REFERENCES $TABLE_ALERTA(id)," +
                    "FOREIGN KEY (id_anotacao) REFERENCES $TABLE_ANOTACAO(id));"
        )

        createTables.forEach { sql ->
            try {
                db?.execSQL(sql)
                Log.i("info_db", "Tabela criada com sucesso: ${sql.split(" ")[2]}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("info_db", "Erro ao criar tabela: ${sql.split(" ")[2]}")
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("info_db", "Executou onUpgrade")

        val dropTables = arrayOf(
            "DROP TABLE IF EXISTS $TABLE_AGENDA",
            "DROP TABLE IF EXISTS $TABLE_ALERTA",
            "DROP TABLE IF EXISTS $TABLE_MEDICAMENTO",
            "DROP TABLE IF EXISTS $TABLE_ENDERECO",
            "DROP TABLE IF EXISTS $TABLE_ANOTACAO",
            "DROP TABLE IF EXISTS $TABLE_USUARIO"
        )

        dropTables.forEach { sql ->
            try {
                db?.execSQL(sql)
                Log.i("info_db", "Tabela removida com sucesso: ${sql.split(" ")[4]}")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.i("info_db", "Erro ao remover tabela: ${sql.split(" ")[4]}")
            }
        }

        onCreate(db)
    }
}
