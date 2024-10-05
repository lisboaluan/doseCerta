package com.luanlisboa.dosecerta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.luanlisboa.dosecerta.R
import com.luanlisboa.dosecerta.repository.UsuarioRepository
import com.luanlisboa.dosecerta.router.RouterManager
import com.luanlisboa.dosecerta.utils.SessionManager

// Constantes para parâmetros de inicialização do fragmento
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento que exibe o perfil do usuário.
 */
class PerfilFragment : Fragment() {

    // Parâmetros opcionais para passagem de argumentos no fragmento
    private var param1: String? = null
    private var param2: String? = null

    // Variáveis para as views e o repositório de usuário
    private lateinit var tvSaudacaoUsuario: TextView
    private lateinit var ivIconeUsuario: ImageView
    private lateinit var btnLogout: Button
    private lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Verifica se o fragmento recebeu argumentos e os define
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout do fragmento
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializa as views
        tvSaudacaoUsuario = view.findViewById(R.id.tvSaudacaoUsuario)
        ivIconeUsuario = view.findViewById(R.id.ivIconeUsuario)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Inicializa o repositório de usuários
        usuarioRepository = UsuarioRepository(requireContext())

        // Carrega o nome do usuário a partir do banco de dados
        val nomeUsuario = usuarioRepository.buscarNomeUsuario(SessionManager.loggedInUserId)
        nomeUsuario?.let {
            // Atualiza o TextView com o nome do usuário
            tvSaudacaoUsuario.text = "Olá, $it"
        }

        // Configura o botão de logout
        btnLogout.setOnClickListener {
            // Realiza logout e redireciona para a tela de login
            SessionManager.logout(requireContext())
            RouterManager.direcionarParaLogin(requireActivity())
        }

        return view
    }

    companion object {
        /**
         * Método de fábrica para criar uma nova instância do PerfilFragment
         * usando os parâmetros fornecidos.
         *
         * @param param1 Parâmetro 1.
         * @param param2 Parâmetro 2.
         * @return Uma nova instância de PerfilFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
