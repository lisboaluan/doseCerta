package com.luanlisboa.dosecerta.utils

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.luanlisboa.dosecerta.models.Notificacao
import com.luanlisboa.dosecerta.repositories.AgendaRepository
import com.luanlisboa.dosecerta.repositories.MedicamentoRepository
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
        Log.d("NotificationHelper", "Exibindo notificação com ID: ${notificacao.idNotificacao}")

        // Intent com a ação específica para marcar como tomado
        val marcarComoTomadoIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_MARK_AS_TAKEN" // Defina a ação específica aqui
            putExtra("horaNotificacao", notificacao.horaNotificacao)
            putExtra("dataNotificacao", notificacao.dataNotificacao)
            putExtra("idMedicamento", notificacao.idMedicamento) // Corrigido para "idMedicamento" conforme esperado
        }

        Log.d(
            "NotificationHelper",
            "Intent criada com hora: ${notificacao.horaNotificacao}, data: ${notificacao.dataNotificacao}, idAgenda: ${notificacao.idMedicamento}"
        )

        // PendingIntent com a ação personalizada
        val marcarComoTomadoPendingIntent = PendingIntent.getBroadcast(
            context,
            notificacao.idNotificacao,
            marcarComoTomadoIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construa a notificação com o botão de ação "Tomado"
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(notificacao.titulo)
            .setContentText(notificacao.conteudo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                android.R.drawable.ic_menu_compass,
                "Tomado",
                marcarComoTomadoPendingIntent // O PendingIntent com a ação específica
            )

        // Exibe a notificação
        with(NotificationManagerCompat.from(context)) {
            notify(notificacao.idNotificacao, builder.build())
            Log.d("NotificationHelper", "Notificação exibida com ID ${notificacao.idNotificacao}")
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
            putExtra("idNotificacao", notificacao.idMedicamento)
            putExtra("horaNotificacao", notificacao.horaNotificacao)
            putExtra("dataNotificacao", notificacao.dataNotificacao)
            putExtra("idMedicamento", notificacao.idMedicamento)
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
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            idNotificacao,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        val sharedPrefs = context.getSharedPreferences("ScheduledNotifications", Context.MODE_PRIVATE)
        sharedPrefs.edit().remove(idNotificacao.toString()).apply()

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
            val action = intent.action
            val titulo = intent.getStringExtra("titulo") ?: "Título padrão"
            val conteudo = intent.getStringExtra("conteudo") ?: "Conteúdo padrão"
            val idNotificacao = intent.getIntExtra("idNotificacao", -1)
            val horaNotificacao = intent.getStringExtra("horaNotificacao")
            val dataNotificacao = intent.getStringExtra("dataNotificacao")
            val idMedicamento = intent.getIntExtra("idMedicamento", -1)

            Log.d("NotificationHelper", "Intent criada com hora: $horaNotificacao, data: $dataNotificacao, idNotificacao: $idNotificacao, idAgenda: $idMedicamento")

            if (idNotificacao != -1) {
                val notificacao = Notificacao(
                    idNotificacao = idNotificacao,
                    titulo = titulo,
                    conteudo = conteudo,
                    horaNotificacao = horaNotificacao ?: "",
                    dataNotificacao = dataNotificacao ?: "",
                    idMedicamento
                )
                val notificationHelper = NotificationHelper(context)
                notificationHelper.showNotification(notificacao)
                Log.d("NotificationReceiver", "Renovação da notificação com ID $idNotificacao.")
            }

            // Executar marcarComoTomado somente se a ação for "ACTION_MARK_AS_TAKEN"
            if (action == "ACTION_MARK_AS_TAKEN" && horaNotificacao != null && dataNotificacao != null && idMedicamento != -1) {
                val agendaRepository = AgendaRepository(context)
                val medicamentoRepository =MedicamentoRepository(context)
                medicamentoRepository.decrementarEstoque(idMedicamento.toLong())
                val horaFormatada = horaNotificacao.split(":").let { partes ->
                    if (partes.size >= 2) "${partes[0]}:${partes[1]}" else horaNotificacao
                }

                val dateTimeString = "$dataNotificacao" + "T" + "$horaFormatada"
                agendaRepository.marcarComoTomado(dateTimeString, idMedicamento)
                Log.d("NotificationReceiver", "marcarComoTomado chamado para idAgenda: $idMedicamento com hora: $horaNotificacao e data: $dataNotificacao")
                // Cancelar a notificação após marcar como tomado
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(idNotificacao)
                Log.d("NotificationReceiver", "Notificação com ID $idNotificacao cancelada.")
            } else {
                Log.e("NotificationReceiver", "Dados insuficientes para executar marcarComoTomado.")
            }
        }
    }

}
