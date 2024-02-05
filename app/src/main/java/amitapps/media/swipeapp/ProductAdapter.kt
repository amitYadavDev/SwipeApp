package amitapps.media.swipeapp

import amitapps.media.swipeapp.models.ProductItem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val productList: List<ProductItem>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = filteredData[position]
        holder.bind(product)
    }

    private var filteredData: List<ProductItem> = productList


    fun filter(query: String) {
        filteredData = productList.filter {
            it.product_name.contains(query, ignoreCase = true) ||
            it.product_type.contains(query, ignoreCase = true)
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return filteredData.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val productTypeTextView: TextView = itemView.findViewById(R.id.productTypeTextView)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val productTax: TextView = itemView.findViewById(R.id.productTax)
        private val productImageView: ImageView = itemView.findViewById(R.id.productImageView)

        fun bind(product: ProductItem) {
            productNameTextView.text = "Name: ${product.product_name}"
            productTypeTextView.text = "Type: ${product.product_type}"
            productPriceTextView.text = "Price: Rs.${product.price}"
            productTax.text = "Tax: Rs.${product.tax}"
//            Log.d("productAdapter_abc_adapter", product.product_name)

            // Load image using Glide library, or you can use any other image loading library
            Glide.with(itemView)
                .load(product.image)
                .placeholder(R.drawable.img)
                .into(productImageView)
        }
    }
}
