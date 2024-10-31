package com.luanlisboa.dosecerta.models

data class Agenda (
    val idAgenda: Int = -1,
    val idAlerta: Int = -1,
    val nome: String,
    val hora: String,
    val dosagem: String,
    val situacaoIngestao: Int = 0,
    val dataDeIngestao: String,
    val idMedicamento: Int
)