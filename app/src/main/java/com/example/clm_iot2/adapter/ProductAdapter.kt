import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product
import com.example.clm_iot2.security.TokenManager

class ProductAdapter(
    private var products: List<Product>,
    private val onProductAdded: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProduitViewHolder>() {

    inner class ProduitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconeProduit: ImageView = itemView.findViewById(R.id.iconeProduit)
        val nomProduit: TextView = itemView.findViewById(R.id.nomProduit)
        val descriptionProduct: TextView = itemView.findViewById(R.id.descriptionProduit)
        val prixProduit: TextView = itemView.findViewById(R.id.prixProduit)
        val btnAdd: Button = itemView.findViewById(R.id.btnAjouter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produit, parent, false)
        return ProduitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
        val produit = products[position]

        // Construire l'URL complète de l'image
        val baseUrl = "https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net"
        val fullImageUrl = baseUrl + produit.imageUrl

        val glideUrl = GlideUrl(
            fullImageUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                .build()
        )
        // Utiliser Glide pour charger l'image
        Glide.with(holder.itemView.context)
            .load(glideUrl)
            .into(holder.iconeProduit)

        holder.nomProduit.text = produit.name
        holder.descriptionProduct.text = produit.description
        holder.prixProduit.text = "${produit.price} €"

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("id", produit.id)
                putString("name", produit.name)
                putString("description", produit.description)
                putDouble("price", produit.price)
                putString("imageUrl", produit.imageUrl)
                putInt("stock", produit.stock)
            }

            holder.itemView.findNavController().navigate(
                R.id.action_productsFragment_to_singleProductFragment,
                bundle
            )
        }

        holder.btnAdd.setOnClickListener {
            val produitAvecQuantite = produit.copy(quantity = 1)
            onProductAdded(produitAvecQuantite)
        }
    }

    override fun getItemCount() = products.size
}
