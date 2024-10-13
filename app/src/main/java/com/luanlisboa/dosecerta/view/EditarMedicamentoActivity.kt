package com.luanlisboa.dosecerta.view

import android.os.Bundle
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.repository.MedicamentoRepository

class EditarMedicamentoActivity : AppCompatActivity() {

    private lateinit var medicamentoRepository: MedicamentoRepository
    private var medicamentoId: Long? = null

    private lateinit var editNomeMedicamento: TextInputEditText
    private lateinit var spinnerFormato: Spinner
    private lateinit var editUnidadeMedida: TextInputEditText
    private lateinit var spinnerUnidadeMedida: Spinner
    private lateinit var editEstoque: TextInputEditText
    private lateinit var editEstoqueFormato: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_medicamento)

        // Inicializando o repositório
        medicamentoRepository = MedicamentoRepository(this)

        // Recuperando os campos de input
        editNomeMedicamento = findViewById(R.id.edit_NomeMedicamentoEditar)
        spinnerFormato = findViewById(R.id.spinnerFormatoEditar)
        editUnidadeMedida = findViewById(R.id.edit_unidadeMedidaEditar)
        spinnerUnidadeMedida = findViewById(R.id.spinnerUnidadeMedidaEditar)
        editEstoque = findViewById(R.id.edit_estoqueEditar)
        editEstoqueFormato = findViewById(R.id.spinnerEstoqueEditar)


        // Recupera o ID do medicamento enviado pela Intent
        medicamentoId = intent.getLongExtra("medicamentoId", -1)
        if (medicamentoId != -1L) {
            // Carregar dados do medicamento no formulário
            carregarDadosMedicamento(medicamentoId!!)
        }

        // Configura o botão de salvar
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_salvar).setOnClickListener {
            // Somente salvar quando o botão for clicado
            salvarMedicamento()
        }
    }

    private fun carregarDadosMedicamento(id: Long) {
        // Recupera o medicamento do banco de dados
        val medicamento = medicamentoRepository.getMedicamentoById(id)

        val formatoArray = resources.getStringArray(R.array.formato) // O array usado no Spinner


        // Preenche os campos com os dados existentes
        medicamento?.let {
            editNomeMedicamento.setText(it.nome)
            spinnerFormato.setSelection(formatoArray.indexOf(it.formato))
            editUnidadeMedida.setText(it.medida)
            editEstoque.setText(it.quantEstoque.toString())
        }
    }

    private fun salvarMedicamento() {
        val nome = editNomeMedicamento.text.toString()
        val dosagem = editUnidadeMedida.text.toString()
/**
        if (nome.isEmpty() || dosagem.isEmpty() || horario.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (medicamentoId != null && medicamentoId != -1L) {
            // Atualizar o medicamento existente
            medicamentoRepository.atualizarMedicamento(medicamentoId!!, nome, dosagem, horario)
            Toast.makeText(this, "Medicamento atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        }
**/
        // Finaliza a Activity após salvar
        finish()
    }
}
