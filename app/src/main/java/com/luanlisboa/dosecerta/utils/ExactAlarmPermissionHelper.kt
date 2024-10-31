package com.luanlisboa.dosecerta.utils

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

object ExactAlarmPermissionHelper {

    /**
     * Verifica se o aplicativo tem permissão para alarmes exatos.
     * Caso contrário, solicita ao usuário que conceda essa permissão nas configurações.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun checkAndRequestExactAlarmPermission(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (!alarmManager.canScheduleExactAlarms()) {
            // Exibe um diálogo explicativo e redireciona para as configurações
            AlertDialog.Builder(context)
                .setTitle("Permissão Necessária")
                .setMessage("Este aplicativo precisa da permissão para alarmes exatos para funcionar corretamente. Deseja permitir essa permissão nas configurações?")
                .setPositiveButton("Sim") { _, _ ->
                    val intent = Intent(
                        Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                        Uri.parse("package:${context.packageName}")
                    )
                    context.startActivity(intent)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}