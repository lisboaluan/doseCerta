package com.luanlisboa.dosecerta.models

data class Alerta(
    val id: Int,
    val periodicidade: String,
    val horarioPrimeiraDose: String,
    val dosagem: String,
    )