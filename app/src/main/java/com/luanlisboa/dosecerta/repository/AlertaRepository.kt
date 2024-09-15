package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.models.Alerta
import com.luanlisboa.dosecerta.utils.SessionManager

class AlertaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirAlerta(periodicidade: String, horarioPrimeiraDose: String, diasTratamento: String, dosagem: String, notificar: Int, idMedicamento: Long): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("periodicidade", periodicidade)
            put("horarioPrimeiraDose", horarioPrimeiraDose)
            put("diasTratamento", diasTratamento)
            put("dosagem", dosagem)
            put("notificar", notificar)
            put("id_usuario", SessionManager.loggedInUserId)
            put("id_medicamento", idMedicamento)
        }

        val resultado = db.insert("tbl_Alerta", null, contentValues)
        db.close()
        return resultado
    }

    fun getAllAlertas(): List<Alerta> {
        val alertas = mutableListOf<Alerta>()
        val db = dbHelper.readableDatabase
        val cursor = db.query("tbl_Alerta", null, "id_usuario = ?", arrayOf( SessionManager.loggedInUserId.toString()), null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val periodicidade = cursor.getString(cursor.getColumnIndexOrThrow("periodicidade"))
                val horarioPrimeiraDose = cursor.getString(cursor.getColumnIndexOrThrow("horarioPrimeiraDose"))
                val dosagem = cursor.getString(cursor.getColumnIndexOrThrow("dosagem"))
                alertas.add(Alerta(periodicidade, horarioPrimeiraDose, dosagem))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return alertas
    }
}

