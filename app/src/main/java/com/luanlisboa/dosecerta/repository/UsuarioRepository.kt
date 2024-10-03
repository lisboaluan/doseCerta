package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper

class UsuarioRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirUsuario(nome: String, dataNascimento: String, genero: String, email: String, senha: String): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", nome)
            put("senha", senha)
            put("email", email)
            put("dataNascimento", dataNascimento)
            put("genero", genero)
        }

        val resultado = db.insert("tbl_Usuario", null, contentValues)
        db.close()
        return resultado
    }

    fun validarUsuario(email: String, senha: String): Int? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val query = "SELECT * FROM tbl_Usuario WHERE email = ? AND senha = ?"
        val cursor = db.rawQuery(query, arrayOf(email, senha))

        var idUsuario: Int? = null

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id")
            idUsuario = cursor.getInt(idIndex)
        }

        cursor.close()
        db.close()

        return idUsuario
    }

    fun buscarUsuarioPorId(id: Int): Int? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val query = "SELECT * FROM tbl_Usuario WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        var idUsuario: Int? = null

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id")
            idUsuario = cursor.getInt(idIndex)
        }

        cursor.close()
        db.close()

        return idUsuario
    }
}
