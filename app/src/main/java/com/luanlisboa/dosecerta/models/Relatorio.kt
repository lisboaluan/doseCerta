package com.luanlisboa.dosecerta.models

data class Relatorio(
    val data: String, // Data da entrada
    val nomeMedicamento: String,
    val horaDosagem: String,
    val situacaoIngestao: String // "Tomado" ou "Não tomado"
)

