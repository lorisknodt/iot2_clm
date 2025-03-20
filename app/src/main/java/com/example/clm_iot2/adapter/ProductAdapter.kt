import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product

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
        holder.iconeProduit.setImageResource(produit.iconeResId)
        holder.nomProduit.text = produit.nom
        holder.descriptionProduct.text = produit.description
        holder.prixProduit.text = "${produit.prix} €"

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.action_productsFragment_to_singleProductFragment)
        }

        holder.btnAdd.setOnClickListener {
            val produitAvecQuantite = produit.copy(quantity = 1) // S'assure que la quantité est définie correctement
            onProductAdded(produitAvecQuantite)
        }
    }

    fun updateData(newProduits: List<Product>) {
        products = newProduits
        notifyDataSetChanged()
    }

    override fun getItemCount() = products.size
}
