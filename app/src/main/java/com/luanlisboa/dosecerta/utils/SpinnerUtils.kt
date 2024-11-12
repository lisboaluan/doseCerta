package com.luanlisboa.dosecerta.utils

import android.content.res.Resources
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.luanlisboa.dosecerta.R

object SpinnerUtils {

    fun setupFormatoSpinnerListener(
        formatoSpinner: Spinner,
        unidadeMedidaSpinner: Spinner,
        resources: Resources
    ) {
        formatoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val formatoSelecionado = formatoSpinner.selectedItem.toString()

                when (formatoSelecionado) {
                    "Cápsula(s)", "Comprimido(s)", "Drágea(s)" -> {
                        val indexMg = resources.getStringArray(R.array.unidadeMedida).indexOf("mg")
                        unidadeMedidaSpinner.setSelection(indexMg)
                    }
                    "Frasco(s)" -> {
                        val indexMl = resources.getStringArray(R.array.unidadeMedida).indexOf("ml")
                        unidadeMedidaSpinner.setSelection(indexMl)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não é necessário implementar
            }
        }
    }
}
