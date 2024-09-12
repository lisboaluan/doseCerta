package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import java.time.LocalDateTime

class AgendaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirAgenda(dataHora: String, situacaoIngestao: Int, idAlerta: Long, idAnotacao: Long): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("dataHora", dataHora)
            put("situacaoIngestao", situacaoIngestao)
            put("id_alerta", idAlerta)
            put("id_anotacao", idAnotacao)
        }

        val resultado = db.insert("tbl_Agenda", null, contentValues)
        db.close()
        return resultado
    }
}
