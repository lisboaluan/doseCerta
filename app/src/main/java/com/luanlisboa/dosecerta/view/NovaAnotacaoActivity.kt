package com.luanlisboa.dosecerta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class NovaAnotacaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anotacao)

        val btnVoltarAnotacao: MaterialButton = findViewById(R.id.btn_voltarAnotacao)

        btnVoltarAnotacao.setOnClickListener {
            finish()
        }
    }

}