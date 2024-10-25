package com.luanlisboa.dosecerta.models

data class Agenda (
    val nome: String,
    val hora: String,
    val dosagem: String,
    val situacaoIngestao: Int = 0,
    val dataDeIngestao: String
)
