package com.luanlisboa.dosecerta.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.database.DatabaseHelper
import com.luanlisboa.dosecerta.models.Agenda
import com.luanlisboa.dosecerta.models.Tratamento
import com.luanlisboa.dosecerta.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgendaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirAgenda(dataHora: String, situacaoIngestao: Int, idAlerta: Long, idAnotacao: Long): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        // Formato da data/hora que está sendo passado (ajuste conforme necessário)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val dataHoraAtual = Date()

        // Parse da dataHora passada para um objeto Date
        val dataHoraInserida = dateFormat.parse(dataHora)

        // Define situação de ingestão como 1 se a dataHora for anterior ao momento atual
        val situacaoIngestaoFinal = if (dataHoraInserida != null && dataHoraInserida.before(dataHoraAtual)) {
            1
        }else{
            situacaoIngestao
        }

        // Prepara os valores para inserção
        val contentValues = ContentValues().apply {
            put("dataHora", dataHora)
            put("situacaoIngestao", situacaoIngestaoFinal)
            put("id_alerta", idAlerta)
            put("id_anotacao", idAnotacao)
        }

        // Insere os valores no banco de dados
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
    SELECT tbl_Alerta.id AS id_alerta,  
           tbl_Medicamento.nome, 
           time(tbl_Agenda.dataHora) AS hora, 
           tbl_Alerta.dosagem,
           date(tbl_Agenda.dataHora) AS dataDeIngestao,
           tbl_Agenda.situacaoIngestao,
           tbl_Medicamento.id AS id_medicamento  -- Adiciona o id do medicamento
    FROM tbl_Agenda
    JOIN tbl_Alerta ON tbl_Agenda.id_alerta = tbl_Alerta.id
    JOIN tbl_Medicamento ON tbl_Alerta.id_medicamento = tbl_Medicamento.id
    WHERE date(tbl_Agenda.dataHora) = ?
    AND tbl_Medicamento.id_usuario = ?
    ORDER BY time(tbl_Agenda.dataHora) ASC
"""

        val cursor = db.rawQuery(query, arrayOf(data, SessionManager.loggedInUserId.toString()))

        val agendas = mutableListOf<Agenda>()
        if (cursor.moveToFirst()) {
            do {
                val agenda = Agenda(
                    idAlerta = cursor.getString(cursor.getColumnIndexOrThrow("id_alerta")).toInt(),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),  // Nome do medicamento
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),  // Hora da dose
                    dosagem = cursor.getString(cursor.getColumnIndexOrThrow("dosagem")),  // Dosagem
                    situacaoIngestao = cursor.getInt(cursor.getColumnIndexOrThrow("situacaoIngestao")),  // Situação de ingestão
                    dataDeIngestao = cursor.getString(cursor.getColumnIndexOrThrow("dataDeIngestao")), // Data de ingestão
                    idMedicamento = cursor.getString(cursor.getColumnIndexOrThrow("id_medicamento")).toInt()
                )
                agendas.add(agenda)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return agendas
    }

    fun marcarComoTomado(agendaId: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("situacaoIngestao", 1) // Atualiza o campo para indicar que o medicamento foi tomado
        }

        val selection = "id = ?"
        val selectionArgs = arrayOf(agendaId.toString())

        db.update("tbl_Agenda", values, selection, selectionArgs)
        db.close()
    }
}

