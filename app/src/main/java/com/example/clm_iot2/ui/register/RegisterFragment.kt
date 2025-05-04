package com.example.clm_iot2.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.clm_iot2.LoginActivity
import com.example.clm_iot2.R
import com.example.clm_iot2.data.network.RetrofitInstanceAuth
import com.example.clm_iot2.model.AccountRequest
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class RegisterFragment : Fragment() {

    private lateinit var etFirstName: TextInputEditText
    private lateinit var etLastName: TextInputEditText
    private lateinit var etBirthDate: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var tilBirthDate: TextInputLayout
    private lateinit var btnRegister: MaterialButton
    private lateinit var btnCancel: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("RegisterFragment", "onCreateView called")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        Log.d("RegisterFragment", "onViewCreated called")

        // Initialiser les vues
        etFirstName = view.findViewById(R.id.et_first_name)
        etLastName = view.findViewById(R.id.et_last_name)
        etBirthDate = view.findViewById(R.id.et_birth_date)
        etEmail = view.findViewById(R.id.et_register_email)
        etPassword = view.findViewById(R.id.et_register_password)
        etConfirmPassword = view.findViewById(R.id.et_confirm_password)
        tilBirthDate = view.findViewById(R.id.til_birth_date)
        btnRegister = view.findViewById(R.id.btn_confirm_register)
        btnCancel = view.findViewById(R.id.btn_cancel_register)

        // Configurer le sélecteur de date
        setupDatePicker()

        // Configurer les boutons
        setupButtons()
    }

    private fun setupDatePicker() {
        etBirthDate.isFocusable = false
        etBirthDate.isClickable = true

        etBirthDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Sélectionnez votre date de naissance")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                etBirthDate.setText(dateFormat.format(Date(selection)))
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
    }

    private fun setupButtons() {
        btnRegister.setOnClickListener {
            if (validateInputs()) {
                val firstName = etFirstName.text.toString().trim()
                val lastName = etLastName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val birthDate = etBirthDate.text.toString().trim()
                val fullName = "$firstName $lastName"

                val formattedBirthDate = convertDateFormatModern(birthDate)
                if (formattedBirthDate != null) {
                    val accountRequest = AccountRequest(
                        clientName = fullName,
                        password = password,
                        birthDate = formattedBirthDate,
                        email = email,
                        role = "ADMIN"
                    )
                    // Lancer une coroutine pour l’appel API
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            val response = withContext(Dispatchers.IO) {
                                RetrofitInstanceAuth.api.createAccount(accountRequest)
                            }

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Compte créé avec succès", Toast.LENGTH_SHORT).show()

                                // Rediriger vers LoginActivity
                                val intent = Intent(context, LoginActivity::class.java)
                                startActivity(intent)
                                activity?.finish() // Pour éviter de revenir à l'écran d'inscription

                            } else {
                                Toast.makeText(context, "Erreur : ${response.code()}", Toast.LENGTH_SHORT).show()
                                Log.d("RegisterFragment", response.message())
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "Erreur réseau : ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        btnCancel.setOnClickListener {
            // Retourner à l'écran de connexion
            activity?.let { act ->
                // Rétablir la visibilité des éléments de l'écran de connexion
                act.findViewById<CardView>(R.id.card_login)?.visibility = View.VISIBLE
                act.findViewById<ImageView>(R.id.iv_app_logo)?.visibility = View.VISIBLE
                act.findViewById<TextView>(R.id.tv_app_name)?.visibility = View.VISIBLE
                act.findViewById<TextView>(R.id.tv_subtitle)?.visibility = View.VISIBLE
                act.findViewById<TextView>(R.id.tv_help)?.visibility = View.VISIBLE

                // Masquer le conteneur de fragment
                act.findViewById<FrameLayout>(R.id.register_fragment_container)?.visibility = View.GONE

                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        // Valider prénom
        if (etFirstName.text.toString().trim().isEmpty()) {
            etFirstName.error = "Le prénom est requis"
            isValid = false
        }

        // Valider nom
        if (etLastName.text.toString().trim().isEmpty()) {
            etLastName.error = "Le nom est requis"
            isValid = false
        }

        // Valider date de naissance
        if (etBirthDate.text.toString().trim().isEmpty()) {
            etBirthDate.error = "La date de naissance est requise"
            isValid = false
        }

        // Valider email
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            etEmail.error = "L'email est requis"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Email invalide"
            isValid = false
        }

        // Valider mot de passe
        val password = etPassword.text.toString()
        if (password.isEmpty()) {
            etPassword.error = "Le mot de passe est requis"
            isValid = false
        } else if (password.length < 6) {
            etPassword.error = "Le mot de passe doit contenir au moins 6 caractères"
            isValid = false
        }

        // Valider confirmation du mot de passe
        if (etConfirmPassword.text.toString() != password) {
            etConfirmPassword.error = "Les mots de passe ne correspondent pas"
            isValid = false
        }

        return isValid
    }


    companion object {
        fun newInstance() = RegisterFragment()
    }
    fun convertDateFormatModern(inputDate: String): String? {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val date = LocalDate.parse(inputDate, inputFormatter)
            date.format(outputFormatter)
        } catch (e: Exception) {
            null
        }
    }
}