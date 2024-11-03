package com.luanlisboa.dosecerta.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.utils.DatabaseHelper

class UsuarioRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirUsuario(
        nome: String, dataNascimento: String, genero: String, email: String, senha: String
    ): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", nome)
            put("senha", senha)
            put("email", email)
            put("dataNascimento", dataNascimento)
            put("genero", genero)
        }
        return db.insert("tbl_Usuario", null, contentValues)
    }

    fun validarUsuario(email: String, senha: String): Int? {
        val db = dbHelper.readableDatabase
        val query = "SELECT id FROM tbl_Usuario WHERE email = ? AND senha = ?"
        val cursor = db.rawQuery(query, arrayOf(email, senha))

        var idUsuario: Int? = null

        if (cursor.moveToFirst()) {
            idUsuario = cursor.getInt(0) // Obtem o ID diretamente do índice 0
        }

        cursor.close()
        db.close()

        return idUsuario
    }

    fun buscarIdUsuario(id: Int): Int? {
        val db = dbHelper.readableDatabase

        // Consulta SQL otimizada
        val query = "SELECT id FROM tbl_Usuario WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        var idUsuario: Int? = null
        if (cursor.moveToFirst()) {
            idUsuario = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return idUsuario
    }

    fun buscarNomeUsuario(id: Int): String? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val query = "SELECT nome FROM tbl_Usuario WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val nomeIndex = cursor.getColumnIndex("nome")
            return cursor.getString(nomeIndex)
        }

        cursor.close()
        db.close()

        return null // Retornamos null se o usuário não for encontrado.
    }
}
