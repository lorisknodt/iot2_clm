import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product  // Remplace par le bon import de ton modèle
import com.example.clm_iot2.security.TokenManager

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

        // Construire l'URL complète de l'image
        val baseUrl = "https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net"
        val fullImageUrl = baseUrl + product.imageUrl

        val glideUrl = GlideUrl(
            fullImageUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                .build()
        )
        // Utiliser Glide pour charger l'image
        Glide.with(holder.itemView.context)
            .load(glideUrl)
            .into(holder.iconResId)

        //holder.iconResId.setImageResource(R.drawable.ic_television) //de base product.iconeResId faudra changer si on gère les icones
        holder.productName.text = product.name
        holder.productPrice.text = "${product.price} €"
        holder.productQuantity.text = product.quantity.toString()

        // Gestion de l'ajout d'une unité
        holder.btnIncrease.setOnClickListener {
            if (product.quantity < product.availableStock) {
                product.quantity++
                holder.productQuantity.text = product.quantity.toString()
                onQuantityChange(product, product.quantity)
            } else {
                Toast.makeText(holder.itemView.context,
                    holder.itemView.context.getString(R.string.toast_max_stock_reached, product.availableStock.toString()), Toast.LENGTH_SHORT).show()
            }
        }


        // Gestion de la diminution de la quantité
        holder.btnDecrease.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.productQuantity.text = product.quantity.toString()
                onQuantityChange(product, product.quantity)
            }
        }

        // Gestion de la suppression complète du produit
        holder.btnRemove.setOnClickListener {
            onRemoveProduct(product) // La suppression est déjà gérée ici
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, produits.size)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", product.name)
                putString("description", product.description)
                putDouble("price", product.price)
                putInt("imageResId", R.drawable.ic_television)  // Si c'est une ressource locale
            }

            holder.itemView.findNavController().navigate(
                R.id.action_cartFragment_to_singleProductFragment,
                bundle
            )
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
        val iconResId: ImageView = itemView.findViewById(R.id.iconeProductCart)
        val productName: TextView = itemView.findViewById(R.id.nomProduitCart)
        val productPrice: TextView = itemView.findViewById(R.id.prixProduitCart)
        val productQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecrease)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncrease)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }
}
