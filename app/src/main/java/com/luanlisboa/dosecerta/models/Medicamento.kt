package com.luanlisboa.dosecerta.models

data class Medicamento(
    val id: Long? = -1L,  // Valor padrão -1L se não for informado
    val nome: String = "",
    val formato: String = "-1",  // Valor padrão "-1" se não for informado
    val medida: String = "-1", // Valor padrão "-1" se não for informado
    val unidMedida: String = "-1", // Valor padrão "-1" se não for informado
    val quantEstoque: Int = -1, // Valor padrão -1 se não for informado
    val formatoEstoque: String? = null, // Valor padrão null se não for informado
    val horarioPrimeiraDose: String? = null
)