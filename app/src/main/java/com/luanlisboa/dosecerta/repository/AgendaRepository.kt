package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.models.Agenda
import com.luanlisboa.dosecerta.models.Tratamento
import com.luanlisboa.dosecerta.utils.SessionManager

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

    fun obterAgendasPorData(data: String): List<Agenda> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val query = """
        SELECT tbl_Medicamento.nome, 
               time(tbl_Agenda.dataHora) AS hora, 
               tbl_Alerta.dosagem 
        FROM tbl_Agenda
        JOIN tbl_Alerta ON tbl_Agenda.id_alerta = tbl_Alerta.id
        JOIN tbl_Medicamento ON tbl_Alerta.id_medicamento = tbl_Medicamento.id
        WHERE date(tbl_Agenda.dataHora) = ?
        AND tbl_Medicamento.id_usuario = ?
        ORDER BY time(tbl_Agenda.dataHora) ASC
    """
        val cursor = db.rawQuery(query, arrayOf(data,SessionManager.loggedInUserId.toString()))

        val agendas = mutableListOf<Agenda>()
        if (cursor.moveToFirst()) {
            do {
                val agenda = Agenda(
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),  // Nome do medicamento de tbl_Medicamento
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),  // Hora da dose de tbl_Agenda
                    dosagem = cursor.getString(cursor.getColumnIndexOrThrow("dosagem"))  // Dosagem de tbl_Alerta
                )
                agendas.add(agenda)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return agendas
    }

}

