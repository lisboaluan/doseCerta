package com.luanlisboa.dosecerta.utils

import android.content.Context
import android.content.Intent
import com.luanlisboa.dosecerta.views.fragments.TratamentoFragment
import com.luanlisboa.dosecerta.views.activities.CadastroAlertaActivity
import com.luanlisboa.dosecerta.views.activities.CadastroMedicamentoActivity
import com.luanlisboa.dosecerta.views.activities.SignupActivity
import com.luanlisboa.dosecerta.views.activities.LoginActivity
import com.luanlisboa.dosecerta.views.activities.HomeActivity

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

    fun direcionarParaCadastroMedicamento(fragment: TratamentoFragment) {
        val intent = Intent(fragment.requireContext(), CadastroMedicamentoActivity::class.java)
        fragment.startActivity(intent)
    }

    fun direcionarParaCadastroAlerta(context: Context, idMedicamento: Long) {
        val intent = Intent(context, CadastroAlertaActivity::class.java)
        intent.putExtra("idMedicamento", idMedicamento)
        context.startActivity(intent)
    }
}

