package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.models.Anotacoes
import com.luanlisboa.dosecerta.utils.SessionManager

class AnotacaoRepository(context: Context) {

    // Helper para gerenciar o banco de dados SQLite
    private val dbHelper = DatabaseHelper(context)

    /**
     * Insere uma nova anotação no banco de dados para o usuário logado.
     *
     * @param titulo O título da anotação.
     * @param mensagem O conteúdo da anotação.
     * @return O ID da linha inserida ou -1 em caso de erro.
     */
    fun inserirAnotacao(titulo: String, mensagem: String): Long {
        // Obtém um banco de dados gravável
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("titulo", titulo)
            put("mensagem", mensagem)
            put("id_usuario", SessionManager.loggedInUserId)
        }

        // Insere os valores na tabela tbl_Anotacao
        val resultado = db.insert("tbl_Anotacao", null, contentValues)
        db.close()
        return resultado
    }

    /**
     * Busca todas as anotações do usuário logado, ordenadas pela data de criação.
     *
     * @return Uma lista de objetos [Anotacoes].
     */
    fun getAllAnotacoes(): List<Anotacoes> {
        val anotacoes = mutableListOf<Anotacoes>()
        val db = dbHelper.readableDatabase

        // Realiza uma consulta na tabela tbl_Anotacao filtrando pelo id_usuario e ordenando pela data de criação
        val cursor = db.query(
            "tbl_Anotacao",
            arrayOf("titulo", "mensagem", "data_criacao"),
            "id_usuario = ?",
            arrayOf(SessionManager.loggedInUserId.toString()),
            null, null, "data_criacao DESC"
        )

        // Verifica se há registros retornados
        if (cursor.moveToFirst()) {
            do {
                // Obtém os valores das colunas
                val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                val mensagem = cursor.getString(cursor.getColumnIndexOrThrow("mensagem"))
                val dataCriacao = cursor.getString(cursor.getColumnIndexOrThrow("data_criacao"))

                // Adiciona a anotação à lista
                anotacoes.add(Anotacoes(titulo, mensagem, dataCriacao))
            } while (cursor.moveToNext()) // Move para o próximo registro
        }

        cursor.close()
        db.close()

        return anotacoes
    }
}
