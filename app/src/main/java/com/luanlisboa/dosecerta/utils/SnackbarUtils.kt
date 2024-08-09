package com.luanlisboa.dosecerta.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {

        // Função pra exibir mensagem de validação na parte inferior da tela

        fun mensagem(view: View, mensagem: String){
        val snackbar = Snackbar.make(view,mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#1F2B5B"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }
}