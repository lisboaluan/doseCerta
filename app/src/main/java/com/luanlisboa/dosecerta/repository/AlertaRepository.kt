package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
class AlertaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirAlerta(periodicidade: String, horarioPrimeiraDose: String, diasTratamento: String, dosagem: String, notificar: Int, idUsuario: Long, idMedicamento: Long): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("periodicidade", periodicidade)
            put("horarioPrimeiraDose", horarioPrimeiraDose)
            put("diasTratamento", diasTratamento)
            put("dosagem", dosagem)
            put("notificar", notificar)
            put("id_usuario", idUsuario)
            put("id_medicamento", idMedicamento)
        }

        val resultado = db.insert("tbl_Alerta", null, contentValues)
        db.close()
        return resultado
    }
}

