package com.luanlisboa.dosecerta.models

data class Notificacao(
    val idNotificacao: Int = -1,
    val titulo: String,
    val conteudo: String,
    val horaNotificacao: String,  // Hora para a notificação, no formato "HH:mm"
    val dataNotificacao: String,  // Data para a notificação, no formato "yyyy-MM-dd"
    val idMedicamento: Int
)
