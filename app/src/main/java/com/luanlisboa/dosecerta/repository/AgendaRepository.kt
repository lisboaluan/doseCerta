package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.models.Alerta

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

    fun deletarAgenda(idAlerta: Long): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val resultado = db.delete("tbl_Agenda", "id_alerta = ?", arrayOf(idAlerta.toString()))
        db.close()
        return resultado
    }

    fun obterAlertasPorData(data: String): List<Alerta> {
        val db: SQLiteDatabase = dbHelper.readableDatabase

        // Query corrigida para verificar se a coluna dataHora segue o formato correto
        val query = """
        SELECT tbl_Agenda.dataHora, tbl_Agenda.situacaoIngestao, tbl_Alerta.id, tbl_Alerta.periodicidade, 
               tbl_Alerta.horarioPrimeiraDose, tbl_Alerta.dosagem 
        FROM tbl_Agenda
        JOIN tbl_Alerta ON tbl_Agenda.id_alerta = tbl_Alerta.id
        WHERE date(tbl_Agenda.dataHora) = ?
        ORDER BY time(tbl_Agenda.dataHora) ASC
    """

        val cursor = db.rawQuery(query, arrayOf(data))

        val alertas = mutableListOf<Alerta>()
        if (cursor.moveToFirst()) {
            do {
                val alerta = Alerta(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    periodicidade = cursor.getString(cursor.getColumnIndexOrThrow("periodicidade")),
                    horarioPrimeiraDose = cursor.getString(cursor.getColumnIndexOrThrow("horarioPrimeiraDose")),
                    dosagem = cursor.getString(cursor.getColumnIndexOrThrow("dosagem")),
                )
                alertas.add(alerta)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return alertas
    }


}

