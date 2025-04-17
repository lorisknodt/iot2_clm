package com.example.clm_iot2.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Order

class OrderAdapter(
    private var orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId: TextView = itemView.findViewById(R.id.order_id)
        val orderDateTime: TextView = itemView.findViewById(R.id.order_date_time)
        val orderStatus: TextView = itemView.findViewById(R.id.order_status)
        val orderTotal: TextView = itemView.findViewById(R.id.order_total)
        val orderAddress: TextView = itemView.findViewById(R.id.order_address)
        val btnViewDetails: Button = itemView.findViewById(R.id.btnViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        holder.orderId.text = "${order.id}"
        holder.orderDateTime.text = "${order.orderDate} à ${order.orderTime}"
        holder.orderStatus.text = "Statut : ${order.status.capitalize()}"
        holder.orderTotal.text = "${"%.2f".format(order.total)} €"

        val address = order.address
        holder.orderAddress.text = "${address.street} ${address.number}, ${address.zipCode} ${address.city}, ${address.country}"

        // Lors du clic sur un item, envoyer l'ID de la commande via un Bundle et naviguer
        holder.btnViewDetails.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("orderId", order.id) // Mettre l'ID de la commande dans le bundle
            }

            // Naviguer vers le SingleOrderFragment avec l'ID
            holder.itemView.findNavController().navigate(
                R.id.action_orderFragment_to_singleOrderFragment,
                bundle
            )
        }

        setOrderStatus(holder.orderStatus, order.status)
    }

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    fun setOrderStatus(statusTextView: TextView, status: String) {
        // Définir le texte du statut
        statusTextView.text = "Status: $status"

        // Définir le fond approprié en fonction du statut
        val backgroundDrawable = when (status.lowercase()) {
            "confirmed" -> R.drawable.status_badge_pending
            "shipped", "expediee", "expedie" -> R.drawable.status_badge_shipped
            "delivered", "livree", "livre" -> R.drawable.status_badge_delivered
            "cancelled", "annulee", "annule" -> R.drawable.status_badge_cancelled
            else -> R.drawable.status_badge_pending // Par défaut
        }

        statusTextView.setBackgroundResource(backgroundDrawable)
    }

    override fun getItemCount() = orders.size
}
