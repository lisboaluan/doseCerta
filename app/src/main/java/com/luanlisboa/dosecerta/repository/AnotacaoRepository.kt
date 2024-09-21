package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import android.content.Context

class AnotacaoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirAnotacao(titulo: String, mensagem: String):Long{
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("titulo", titulo)
            put("mensagem", mensagem)
        }

        val resultado = db.insert("tbl_Anotacoes", null, contentValues)
        db.close()
        return resultado
    }
}