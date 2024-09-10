package com.luanlisboa.dosecerta.repository

import com.luanlisboa.dosecerta.database.DatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

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
            }

            val resultado = db.insert("tbl_Medicamento", null, contentValues)
            db.close()
            return resultado
        }
    }