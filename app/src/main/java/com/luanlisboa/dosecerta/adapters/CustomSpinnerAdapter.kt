package com.luanlisboa.dosecerta.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.luanlisboa.dosecerta.R

/* Essa classe serve para que o primeiro item do spinner do campo 'formato' da tela de cadastro de medicamentos fique com uma cor cinza,
   parecida com a cor do hint dos demais campos dessa tela. As demais opções do spinner, vão ser exibidos com a cor preta,
   para que se adeque ao comportamento dos demais campos quando são preenchidos pelo usuário */

class CustomSpinnerAdapter(context: Context, private val items: Array<String>) :
    ArrayAdapter<String>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.spinner_itens_formato, parent, false)
        val textView = view.findViewById<TextView>(R.id.text1)

        textView.text = items[position]

        when (position) {
            0 -> textView.setTextColor(Color.GRAY)
            else -> textView.setTextColor(Color.BLACK)
        }

        return view
    }
}