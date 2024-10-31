package com.luanlisboa.dosecerta.view


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.databinding.ActivityHomeBinding
import com.luanlisboa.dosecerta.fragments.AnotacaoFragment
import com.luanlisboa.dosecerta.fragments.PerfilFragment
import com.luanlisboa.dosecerta.fragments.AgendaFragment
import com.luanlisboa.dosecerta.fragments.TratamentoFragment
import com.luanlisboa.dosecerta.repository.AgendaRepository
import com.luanlisboa.dosecerta.utils.NotificationHelper
import java.time.LocalDate


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var agendaRepository: AgendaRepository

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(TratamentoFragment())  // Fragmento inicial
        // Inicializa o NotificationHelper
        notificationHelper = NotificationHelper(this)

        // Solicita permissão (se necessário)
        notificationHelper.requestNotificationPermission(this)
        agendaRepository = AgendaRepository(this)
        agendaRepository.obterAgendasPorData(LocalDate.now().toString(), this)

        supportActionBar?.hide()

        binding.btnNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tratamento -> replaceFragment(TratamentoFragment())
                R.id.menu_relatorio -> replaceFragment(AgendaFragment())
                R.id.menu_anotacao -> replaceFragment(AnotacaoFragment())
                R.id.menu_perfil -> replaceFragment(PerfilFragment())
                else -> replaceFragment(TratamentoFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentMenu, fragment)

        // Adiciona o fragmento à pilha de retorno
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    // Sobrescreve o comportamento do botão "voltar" para evitar fechar a HomeActivity
    override fun onBackPressed() {
        super.onBackPressed()
        // Se não houver mais fragmentos na pilha, minimiza o app ao invés de fechar
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()  // Volta para o fragmento anterior
        } else {
            moveTaskToBack(true)  // Minimiza o app
        }
    }
}
