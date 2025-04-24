/* package com.example.clm_iot2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.User
import com.example.clm_iot2.ui.profile.ProfileViewModel

class ProfileAdapter(
    private var user: User? = null
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val profileInitial: TextView = itemView.findViewById(R.id.tv_profile_initial)
        val clientNumber: TextView = itemView.findViewById(R.id.tv_client_number)
        val memberSince: TextView = itemView.findViewById(R.id.tv_member_since)
        val email: TextView = itemView.findViewById(R.id.tv_email)
        val phone: TextView = itemView.findViewById(R.id.tv_phone)
        val addressStreet: TextView = itemView.findViewById(R.id.tv_address_street)
        val addressCity: TextView = itemView.findViewById(R.id.tv_address_city)
        val addressCountry: TextView = itemView.findViewById(R.id.tv_address_country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        user?.let { u ->
            holder.name.text = u.name
            holder.profileInitial.text = u.name.firstOrNull()?.toString() ?: ""
            holder.clientNumber.text = u.clientAccountNumber
            holder.memberSince.text = holder.itemView.context.getString(
                R.string.member_since,
                ProfileViewModel.formatRegistrationDate(u.registrationDate)
            )
            holder.email.text = u.email
            holder.phone.text = u.phone

            u.address?.let { addr ->
                holder.addressStreet.text = "${addr.street}, ${addr.number}"
                holder.addressCity.text = "${addr.zipCode} ${addr.city}"
                holder.addressCountry.text = addr.country
            }
        }
    }

    override fun getItemCount(): Int = if (user != null) 1 else 0

    fun updateData(newUser: User) {
        user = newUser
        notifyDataSetChanged()
    }
}
*/

// L'ADAPTEUR ICI EST INUTILE CAR LE BUT DES ADAPTEURS EST DE FACILITER LA GESTION DES LISTES DANS UNE INTERFACE.
// ILS LIENT DONC LES MODELES DE DONNEES A L'INTERFACE. VU QUE ICI ON N'UTILISE PAS DE LISTE MAIS JUSTE UN "PROFIL"
// L'UTILISATION D4UN ADAPTER EST INUTILE