package com.luanlisboa.dosecerta.router

import android.content.Context
import android.content.Intent
import com.luanlisboa.dosecerta.view.SignupActivity
import com.luanlisboa.dosecerta.view.LoginActivity
import com.luanlisboa.dosecerta.view.HomeActivity

// Aqui vamos deixar todas funções de direcionamento de telas do app pra evitar ficar repetindo funções

object RouterManager {

        fun direcionarParaCadastro(context: Context){
        val intent = Intent(context, SignupActivity::class.java)
        context.startActivity(intent)
    }

        fun direcionarParaLogin(context: Context){
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

        fun direcionarParaHome(context: Context){
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }
}

