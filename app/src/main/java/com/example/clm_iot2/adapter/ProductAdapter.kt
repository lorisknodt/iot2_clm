import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product

class ProductAdapter(private val produits: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProduitViewHolder>() {

    inner class ProduitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconeProduit: ImageView = itemView.findViewById(R.id.iconeProduit)
        val nomProduit: TextView = itemView.findViewById(R.id.nomProduit)
        val prixProduit: TextView = itemView.findViewById(R.id.prixProduit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produit, parent, false)
        return ProduitViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProduitViewHolder, position: Int) {
        val produit = produits[position]
        holder.iconeProduit.setImageResource(produit.iconeResId)
        holder.nomProduit.text = produit.nom
        holder.prixProduit.text = "${produit.prix} €"
    }

    override fun getItemCount() = produits.size
}
