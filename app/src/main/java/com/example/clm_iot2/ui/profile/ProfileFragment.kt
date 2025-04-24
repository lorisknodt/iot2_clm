package com.example.clm_iot2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.clm_iot2.MainActivity
import com.example.clm_iot2.R
import com.example.clm_iot2.databinding.FragmentProfileBinding
import com.example.clm_iot2.model.User
import com.example.clm_iot2.ui.orders.OrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


import android.content.Intent
import com.example.clm_iot2.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var loadingCard: androidx.cardview.widget.CardView
    private lateinit var profileContentLayout: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileContentLayout = view.findViewById(R.id.profileContentLayout)
        loadingCard = view.findViewById(R.id.profileLoadingCard)

        setupListeners()
        observeViewModel()

        // Charger les données de l'utilisateur
        loadingCard.visibility = View.VISIBLE
        profileContentLayout.visibility = View.GONE

        viewModel.loadUserProfile()
    }

    private fun setupListeners() {
        // Configurer les listeners pour les boutons et autres éléments interactifs
        binding.btnEditAddress.setOnClickListener {
            // Naviguer vers l'écran de modification d'adresse
            Toast.makeText(context, "Modifier l'adresse", Toast.LENGTH_SHORT).show()
        }

        binding.btnOrdersHistory.setOnClickListener {
            // Naviguer vers l'historique des commandes
            Toast.makeText(context, "Historique des commandes", Toast.LENGTH_SHORT).show()
            val navView = requireActivity().findViewById<NavigationView>(R.id.nav_view)
            navView.menu.performIdentifierAction(R.id.nav_orders, 0)
        }

        binding.btnAccountSettings.setOnClickListener {
            // Naviguer vers les paramètres du compte
            Toast.makeText(context, "Paramètres du compte", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_profileFragment_to_developmentFragment)
        }

        binding.btnLogout.setOnClickListener {
            // Déconnexion de l'utilisateur
            viewModel.logout()
            Toast.makeText(context, "Déconnexion...", Toast.LENGTH_SHORT).show()
            // Naviguer vers l'écran de connexion après déconnexion

            // Naviguer vers l'écran de connexion après déconnexion
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)

            // Terminer l'activité actuelle pour empêcher de revenir en arrière
            requireActivity().finish()
        }
    }

    private fun observeViewModel() {
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            profileContentLayout.visibility = View.VISIBLE
            loadingCard.visibility = View.GONE
            user?.let { updateUI(it) }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            profileContentLayout.visibility = View.VISIBLE
            loadingCard.visibility = View.GONE
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.errorMessageShown()
            }
        }
    }

    private fun updateUI(user: User) {
        binding.tvName.text = user.name
        binding.tvProfileInitial.text = user.name.firstOrNull()?.toString() ?: ""
        binding.tvClientNumber.text = user.clientAccountNumber
        binding.tvMemberSince.text = getString(R.string.member_since, viewModel.formatRegistrationDate(user.registrationDate))
        binding.tvEmail.text = user.email
        binding.tvPhone.text = user.phone

        user.address?.let { address ->
            binding.tvAddressStreet.text = "${address.street}, ${address.number}"
            binding.tvAddressCity.text = "${address.zipCode} ${address.city}"
            binding.tvAddressCountry.text = address.country
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}