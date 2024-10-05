package com.luanlisboa.dosecerta.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "UsuarioPrefs"  // Nome do SharedPreferences que você usa
    private const val KEY_USER_ID = "idUsuario"   // Chave para o ID do usuário
    private const val KEY_KEEP_LOGGED_IN = "keepLoggedIn"  // Chave para "manter-se conectado"

    var loggedInUserId: Int = -1  // Variável para manter o ID do usuário em memória

    // Função para obter o SharedPreferences
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Função para salvar a sessão de login (ID do usuário e "manter-se conectado")
    fun saveLoginSession(context: Context, userId: Int, keepLoggedIn: Boolean) {
        loggedInUserId = userId
        val sharedPref = getSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.putBoolean(KEY_KEEP_LOGGED_IN, keepLoggedIn)
        editor.apply()  // Salva as mudanças no SharedPreferences
    }

    // Função para carregar a sessão de login (ID do usuário) do SharedPreferences
    fun loadLoginSession(context: Context) {
        val sharedPref = getSharedPreferences(context)
        loggedInUserId = sharedPref.getInt(KEY_USER_ID, -1)  // Carrega o ID do usuário
    }

    // Verifica se a opção "manter-se conectado" está ativada
    fun isKeepLoggedIn(context: Context): Boolean {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getBoolean(KEY_KEEP_LOGGED_IN, false)
    }

    // Função de logout que limpa tanto a sessão quanto a opção "manter-se conectado"
    fun logout(context: Context) {
        loggedInUserId = -1  // Reseta o ID do usuário na memória
        val sharedPref = getSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.clear().apply()  // Limpa todos os dados no SharedPreferences
    }
}