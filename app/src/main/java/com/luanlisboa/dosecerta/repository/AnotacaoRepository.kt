package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import android.content.Context
import com.luanlisboa.dosecerta.models.Alerta
import com.luanlisboa.dosecerta.models.Anotacoes
import com.luanlisboa.dosecerta.models.Medicamento
import com.luanlisboa.dosecerta.utils.SessionManager

class AnotacaoRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirAnotacao(titulo: String, mensagem: String):Long{
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("titulo", titulo)
            put("mensagem", mensagem)
            put("id_usuario", SessionManager.loggedInUserId)
        }

        val resultado = db.insert("tbl_Anotacao", null, contentValues)
        db.close()
        return resultado
    }

    fun getAllAnotacoes(): List<Anotacoes> {
        val anotacoes = mutableListOf<Anotacoes>()
        val db = dbHelper.readableDatabase
        val cursor = db.query("tbl_Anotacao", null, "id_usuario = ?", arrayOf( SessionManager.loggedInUserId.toString()), null, null, null)

        if (cursor.moveToFirst()) {
            do {

                val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                val mensagem = cursor.getString(cursor.getColumnIndexOrThrow("mensagem"))
                anotacoes.add(Anotacoes(titulo, mensagem))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return anotacoes
    }
}