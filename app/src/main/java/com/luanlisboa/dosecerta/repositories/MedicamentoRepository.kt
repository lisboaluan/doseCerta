package com.luanlisboa.dosecerta.repositories

import com.luanlisboa.dosecerta.utils.DatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.luanlisboa.dosecerta.models.Medicamento
import com.luanlisboa.dosecerta.utils.SessionManager

class MedicamentoRepository (context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun inserirMedicamento(
        nome: String,
        formato: String,
        medida: String,
        unidMedida: String,
        quantEstoque: Int?,
        formatoEstoque: String?
    ): Long {
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
        val cursor = db.query(
            "tbl_Medicamento",
            null,
            "id_usuario = ?",
            arrayOf(SessionManager.loggedInUserId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
                val formato = cursor.getString(cursor.getColumnIndexOrThrow("formato"))
                val medida = cursor.getString(cursor.getColumnIndexOrThrow("medida"))
                val unidMedida = cursor.getLong(cursor.getColumnIndexOrThrow("unidMedida"))
                val quantEstoque = cursor.getString(cursor.getColumnIndexOrThrow("quantEstoque"))
                val formatoEstoque =
                    cursor.getString(cursor.getColumnIndexOrThrow("formatoEstoque"))

                medicamentos.add(
                    Medicamento(
                        id, nome, formato, medida,
                        unidMedida.toString(), if (quantEstoque.isNullOrEmpty()) null else quantEstoque.toInt(), formatoEstoque
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return medicamentos
    }


    fun atualizarMedicamento(
        id: Long,
        nome: String,
        formato: String,
        medida: String,
        unidMedida: String,
        quantEstoque: Int,
        formatoEstoque: String?
    ): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val contentValues = ContentValues().apply {
            put("nome", nome)
            put("formato", formato)
            put("medida", medida)
            put("unidMedida", unidMedida)
            put("quantEstoque", quantEstoque)
            put("formatoEstoque", formatoEstoque)
        }

        // Atualiza o medicamento com o ID correspondente no banco de dados
        val resultado = db.update(
            "tbl_Medicamento",
            contentValues,
            "id = ? AND id_usuario = ?",
            arrayOf(id.toString(), SessionManager.loggedInUserId.toString())
        )

        db.close()
        return resultado
    }

    fun decrementarEstoque(id: Long): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase

        // Obtenha a quantidade atual em estoque do medicamento
        val medicamentoCursor = db.query(
            "tbl_Medicamento",
            arrayOf("quantEstoque"),
            "id = ? AND id_usuario = ?",
            arrayOf(id.toString(), SessionManager.loggedInUserId.toString()),
            null,
            null,
            null
        )

        var resultado = 0
        var quantEstoqueAtual = 0
        if (medicamentoCursor.moveToFirst()) {
            quantEstoqueAtual = medicamentoCursor.getInt(medicamentoCursor.getColumnIndexOrThrow("quantEstoque"))
        }
        medicamentoCursor.close()

        // Obtenha a dosagem na tabela tbl_Alerta
        val alertaCursor = db.query(
            "tbl_Alerta",
            arrayOf("dosagem"),
            "id_medicamento = ? AND id_usuario = ?",
            arrayOf(id.toString(), SessionManager.loggedInUserId.toString()),
            null,
            null,
            null
        )

        var dosagem = 0
        if (alertaCursor.moveToFirst()) {
            val dosagemText = alertaCursor.getString(alertaCursor.getColumnIndexOrThrow("dosagem"))

            // Extrai apenas o número do início da string usando uma expressão regular
            val dosagemNumberMatch = Regex("""\d+""").find(dosagemText)
            dosagem = dosagemNumberMatch?.value?.toInt() ?: 0
        }
        alertaCursor.close()

        // Verifica se a quantidade no estoque é suficiente para a dosagem
        if (quantEstoqueAtual >= dosagem && dosagem > 0) {
            val novoQuantEstoque = quantEstoqueAtual - dosagem
            val contentValues = ContentValues().apply {
                put("quantEstoque", novoQuantEstoque)
            }

            // Atualiza o valor no banco de dados
            resultado = db.update(
                "tbl_Medicamento",
                contentValues,
                "id = ? AND id_usuario = ?",
                arrayOf(id.toString(), SessionManager.loggedInUserId.toString())
            )
        }

        db.close()
        return resultado
    }


    fun deletarMedicamento(id: Long): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val resultado = db.delete(
            "tbl_Medicamento",
            "id = ? AND id_usuario = ?",
            arrayOf(id.toString(), SessionManager.loggedInUserId.toString())
        )
        db.close()
        return resultado
    }

    fun getMedicamentoById(id: Long): Medicamento? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "tbl_Medicamento",
            null,
            "id = ? AND id_usuario = ?",
            arrayOf(id.toString(), SessionManager.loggedInUserId.toString()),
            null,
            null,
            null
        )

        var medicamento: Medicamento? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
            val formato = cursor.getString(cursor.getColumnIndexOrThrow("formato"))
            val medida = cursor.getString(cursor.getColumnIndexOrThrow("medida"))
            val unidMedida = cursor.getString(cursor.getColumnIndexOrThrow("unidMedida"))
            val quantEstoque = cursor.getString(cursor.getColumnIndexOrThrow("quantEstoque"))
            val id_usuario = cursor.getString(cursor.getColumnIndexOrThrow("id_usuario"))

            medicamento = Medicamento(
                id.toLong(),
                nome,
                formato,
                medida,
                unidMedida,
                quantEstoque.toInt(),
                id_usuario.toString()
            )
        }

        cursor.close()
        db.close()

        return medicamento
    }
}