import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product  // Remplace par le bon import de ton modèle

class CartAdapter(
    private val produits: MutableList<Product>,  // Liste des produits dans le panier
    private val onQuantityChange: (Product, Int) -> Unit,  // Callback pour gérer la quantité
    private val onRemoveProduct: (Product) -> Unit          // Callback pour gérer la suppression
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // Création du ViewHolder avec le layout spécifique au panier
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_cart, parent, false)
        return CartViewHolder(view)
    }

    // Liaison des données au ViewHolder
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = produits[position]

        holder.iconeProduit.setImageResource(product.iconeResId)
        holder.nomProduit.text = product.nom
        holder.prixProduit.text = "${product.prix} €"
        holder.tvQuantity.text = product.quantity.toString()

        // Gestion de l'ajout d'une unité
        holder.btnIncrease.setOnClickListener {
            product.quantity++
            holder.tvQuantity.text = product.quantity.toString()
            onQuantityChange(product, product.quantity)
        }

        // Gestion de la diminution de la quantité
        holder.btnDecrease.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.tvQuantity.text = product.quantity.toString()
                onQuantityChange(product, product.quantity)
            }
        }

        // Gestion de la suppression complète du produit
        holder.btnRemove.setOnClickListener {
            onRemoveProduct(product)
            produits.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Nombre total d'éléments dans la liste
    override fun getItemCount(): Int = produits.size

    fun updateData(newProducts: List<Product>) {
        produits.clear()                 // On vide l'ancienne liste
        produits.addAll(newProducts)     // On ajoute les nouvelles données
        notifyDataSetChanged()           // Notifie le RecyclerView que les données ont changé
    }


    // ViewHolder pour les éléments du panier
    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconeProduit: ImageView = itemView.findViewById(R.id.iconeProduitCart)
        val nomProduit: TextView = itemView.findViewById(R.id.nomProduitCart)
        val prixProduit: TextView = itemView.findViewById(R.id.prixProduitCart)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }
}
