package com.luanlisboa.dosecerta.repositories

import android.content.ContentValues
import com.luanlisboa.dosecerta.utils.DatabaseHelper
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.models.Relatorio

class RelatorioRepository(context: Context) {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)

    fun buscarRelatoriosPorPeriodo(dataInicio: String, dataFim: String): List<Relatorio> {
        val db = dbHelper.readableDatabase // Agora usamos dbHelper para acessar o banco de dados
        val query = """
            SELECT 
    substr(A.dataHora, 1, 10) AS data, -- Extrai apenas a data (yyyy-MM-dd)
    M.nome AS nomeMedicamento, -- Nome do medicamento
    substr(A.dataHora, 12, 5) AS horaDosagem, -- Extrai a hora no formato HH:mm
    CASE A.situacaoIngestao 
        WHEN 1 THEN 'Tomado' 
        ELSE 'Não tomado' 
    END AS situacaoIngestao -- Converte 1/0 para "Tomado" ou "Não tomado"
FROM tbl_Agenda A
INNER JOIN tbl_Medicamento M ON A.id_medicamento = M.id -- Relaciona a agenda com o medicamento
WHERE substr(A.dataHora, 1, 10) BETWEEN ? AND ? -- Filtra pelo intervalo de datas
ORDER BY A.dataHora; -- Ordena por data e hora
"""
        val cursor = db.rawQuery(query, arrayOf(dataInicio, dataFim))

        val relatorios = mutableListOf<Relatorio>()
        while (cursor.moveToNext()) {
            val dataDose = cursor.getString(cursor.getColumnIndexOrThrow("data"))
            val nomeMedicamento = cursor.getString(cursor.getColumnIndexOrThrow("nomeMedicamento"))
            val horaDosagem = cursor.getString(cursor.getColumnIndexOrThrow("horaDosagem"))
            val situacaoIngestao = cursor.getString(cursor.getColumnIndexOrThrow("situacaoIngestao"))

            relatorios.add(Relatorio(dataDose, nomeMedicamento, horaDosagem, situacaoIngestao))
        }

        cursor.close()
        db.close()
        return relatorios
    }
}
