package com.luanlisboa.dosecerta.utils

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.luanlisboa.dosecerta.models.Notificacao
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "CANAL_ID"
        private const val CHANNEL_NAME = "Canal de Notificação"
        private const val CHANNEL_DESCRIPTION = "Descrição do Canal"
        private const val REQUEST_CODE_NOTIFICATION = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun isNotificationScheduled(idNotificacao: Int): Boolean {
        val sharedPrefs = context.getSharedPreferences("ScheduledNotifications", Context.MODE_PRIVATE)
        return sharedPrefs.contains(idNotificacao.toString())
    }

    fun markNotificationAsScheduled(idNotificacao: Int) {
        val sharedPrefs = context.getSharedPreferences("ScheduledNotifications", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(idNotificacao.toString(), true).apply()
    }

    fun showNotification(notificacao: Notificacao) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(notificacao.titulo)
            .setContentText(notificacao.conteudo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(notificacao.idNotificacao, builder.build())
        }
    }

    fun scheduleNotification(notificacao: Notificacao) {
        if (isNotificationScheduled(notificacao.idNotificacao)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
            return
        }

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("titulo", notificacao.titulo)
            putExtra("conteudo", notificacao.conteudo)
            putExtra("idNotificacao", notificacao.idNotificacao)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificacao.idNotificacao,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dateTimeString = "${notificacao.dataNotificacao} ${notificacao.horaNotificacao}"
        val triggerTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateTimeString)?.time

        if (triggerTime != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            markNotificationAsScheduled(notificacao.idNotificacao)
        }
    }

    fun cancelNotification(idNotificacao: Int) {
        // Remove o PendingIntent para cancelar o alarme agendado
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            idNotificacao,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent) // Cancela o alarme associado

        // Remove a notificação agendada dos SharedPreferences
        val sharedPrefs = context.getSharedPreferences("ScheduledNotifications", Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(idNotificacao.toString()).apply()

        // Cancela a notificação ativa, caso ainda esteja presente
        NotificationManagerCompat.from(context).cancel(idNotificacao)
    }

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_NOTIFICATION
                )
            }
        }
    }

    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val titulo = intent.getStringExtra("titulo") ?: "Título padrão"
            val conteudo = intent.getStringExtra("conteudo") ?: "Conteúdo padrão"
            val idNotificacao = intent.getIntExtra("idNotificacao", -1)

            if (idNotificacao != -1) {
                val notificacao = Notificacao(
                    idNotificacao = idNotificacao,
                    titulo = titulo,
                    conteudo = conteudo,
                    horaNotificacao = "", // Hora e data já estão incluídas para agendamento, então deixamos em branco
                    dataNotificacao = "",
                    idMedicamento = 0 // ID do medicamento não é necessário aqui
                )
                val notificationHelper = NotificationHelper(context)
                notificationHelper.showNotification(notificacao)
            }
        }
    }
}
