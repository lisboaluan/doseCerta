package com.luanlisboa.dosecerta.repository

import com.luanlisboa.dosecerta.database.DatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.models.Medicamento
import com.luanlisboa.dosecerta.utils.SessionManager

class MedicamentoRepository (context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirMedicamento(nome: String, formato: String, medida: String, unidMedida: String, quantEstoque: Int, formatoEstoque: String?): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", nome)
            put("formato", formato)
            put("medida", medida)
            put("unidMedida", unidMedida)
            put("quantEstoque", quantEstoque)
            put("formatoEstoque", formatoEstoque)
            put("id_Usuario", SessionManager.loggedInUserId)
        }

        val resultado = db.insert("tbl_Medicamento", null, contentValues)
        db.close()
        return resultado
    }

    fun getAllMedicamentos(): List<Medicamento> {
        val medicamentos = mutableListOf<Medicamento>()
        val db = dbHelper.readableDatabase
        val cursor = db.query("tbl_Medicamento", null, "id_usuario = ?", arrayOf( SessionManager.loggedInUserId.toString()), null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
                val formato = cursor.getString(cursor.getColumnIndexOrThrow("formato"))
                val medida = cursor.getString(cursor.getColumnIndexOrThrow("medida"))
                medicamentos.add(Medicamento(nome, formato, medida))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return medicamentos
    }
}