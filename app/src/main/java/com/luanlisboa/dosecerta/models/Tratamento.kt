package com.luanlisboa.dosecerta.models

data class Tratamento(
    val id: Int,
    val periodicidade: String,
    val horarioPrimeiraDose: String,
    val dosagem: String,
    )