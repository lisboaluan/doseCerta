package com.luanlisboa.dosecerta.models

data class Alerta(
    val id: Long? = -1L,
    val periodicidade: String = "",
    val horarioPrimeiraDose: String = "",
    val diasTratamento: String = "",
    val dosagem: String = "",
    val notificar: Int = -1
)
 //criei essa classe sla pq