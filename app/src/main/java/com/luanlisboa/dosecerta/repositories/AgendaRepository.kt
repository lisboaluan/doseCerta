package com.luanlisboa.dosecerta.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.luanlisboa.dosecerta.utils.DatabaseHelper
import com.luanlisboa.dosecerta.models.Agenda
import com.luanlisboa.dosecerta.models.Notificacao
import com.luanlisboa.dosecerta.utils.ExactAlarmPermissionHelper
import com.luanlisboa.dosecerta.utils.NotificationHelper
import com.luanlisboa.dosecerta.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgendaRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val notificationHelper = NotificationHelper(context)

    fun inserirAgenda(dataHora: String, situacaoIngestao: Int, idAlerta: Long, idAnotacao: Long, idMedicamento:Int): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val dataHoraAtual = Date()
        val dataHoraInserida = dateFormat.parse(dataHora)
        val situacaoIngestaoFinal = if (dataHoraInserida != null && dataHoraInserida.before(dataHoraAtual)) {
            1
        } else {
            situacaoIngestao
        }

        val contentValues = ContentValues().apply {
            put("dataHora", dataHora)
            put("situacaoIngestao", situacaoIngestaoFinal)
            put("id_alerta", idAlerta)
            put("id_anotacao", idAnotacao)
            put("id_medicamento", idMedicamento)
        }

        val resultado = db.insert("tbl_Agenda", null, contentValues)
        db.close()
        return resultado
    }

    fun deletarAgenda(idAlerta: Long): Int {
        val db = dbHelper.readableDatabase
        val idsAgenda = mutableListOf<Int>()

        // 1. Consulta para obter todos os idAgenda associados ao idAlerta
        val selectQuery = "SELECT id FROM tbl_Agenda WHERE id_alerta = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(idAlerta.toString()))
        if (cursor.moveToFirst()) {
            do {
                idsAgenda.add(cursor.getInt(cursor.getColumnIndexOrThrow("id")))
            } while (cursor.moveToNext())
        }
        cursor.close()

        // 2. Cancela as notificações para cada idAgenda obtido
        idsAgenda.forEach { idAgenda ->
            notificationHelper.cancelNotification(idAgenda)
        }

        // 3. Exclui as entradas da agenda associadas ao idAlerta
        db.close()
        val writableDb = dbHelper.writableDatabase
        val resultado = writableDb.delete("tbl_Agenda", "id_alerta = ?", arrayOf(idAlerta.toString()))
        writableDb.close()

        return resultado
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun obterAgendasPorData(data: String, context: Context): List<Agenda> {
        ExactAlarmPermissionHelper.checkAndRequestExactAlarmPermission(context)
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val query = """
             SELECT tbl_Agenda.id AS id_agenda,
                   tbl_Alerta.id AS id_alerta,  
                   tbl_Medicamento.nome, 
                   time(tbl_Agenda.dataHora) AS hora, 
                   tbl_Alerta.dosagem,
                   date(tbl_Agenda.dataHora) AS dataDeIngestao,
                   tbl_Agenda.situacaoIngestao,
                   tbl_Medicamento.id AS id_medicamento
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
                    idAgenda = cursor.getInt(cursor.getColumnIndexOrThrow("id_agenda")),
                    idAlerta = cursor.getInt(cursor.getColumnIndexOrThrow("id_alerta")),
                    nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                    dosagem = cursor.getString(cursor.getColumnIndexOrThrow("dosagem")),
                    situacaoIngestao = cursor.getInt(cursor.getColumnIndexOrThrow("situacaoIngestao")),
                    dataDeIngestao = cursor.getString(cursor.getColumnIndexOrThrow("dataDeIngestao")),
                    idMedicamento = cursor.getInt(cursor.getColumnIndexOrThrow("id_medicamento"))
                )
                agendas.add(agenda)

                // Transformar cada `Agenda` em um objeto `Notificacao`
                val notificacao = Notificacao(
                    idNotificacao = agenda.idAgenda,
                    titulo = "Hora de tomar ${agenda.nome}",
                    conteudo = "Dosagem: ${agenda.dosagem}",
                    horaNotificacao = agenda.hora,
                    dataNotificacao = agenda.dataDeIngestao,
                    idMedicamento = agenda.idMedicamento
                )

                // Agendar a notificação
                notificationHelper.scheduleNotification(notificacao)

            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return agendas
    }

    fun marcarComoTomado(dataHora: String, idAgenda: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        if (dataHora == null) {
            return
        }

        // Consulta para verificar a existência de registros que correspondam ao critério
        val query = """
        SELECT id FROM tbl_Agenda 
        WHERE dataHora = ? AND id_medicamento = ?
    """
        val cursor = db.rawQuery(
            query,
            arrayOf(dataHora, idAgenda.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val agendaId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))

                // Realiza o update para marcar o item como tomado
                val values = ContentValues().apply {
                    put("situacaoIngestao", 1)
                }
                db.update("tbl_Agenda", values, "id = ?", arrayOf(agendaId.toString()))

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
    }


}
