package hr.algebra.surfsafely.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.surfsafely.databinding.ItemImageBuyBinding
import hr.algebra.surfsafely.model.AvatarItem

class BuyAvatarRecycleAdapter(private val avatars: List<AvatarItem>, private val onItemClick: (Long) -> Unit) : RecyclerView.Adapter<BuyAvatarRecycleAdapter.AvatarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBuyBinding.inflate(inflater, parent, false)
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val bitmap = avatars[position].bitmap
        val id = avatars[position].id
        val price = avatars[position].price
        holder.bind(bitmap, id, price)
    }

    override fun getItemCount(): Int {
        return avatars.size
    }

    inner class AvatarViewHolder(private val binding: ItemImageBuyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap?, id: Long?, price: Long?) {
            binding.imageView.setImageBitmap(bitmap)
            binding.price.text = price.toString() + " points"

            binding.btnBuyAvatar.setOnClickListener {
                id?.let { onItemClick.invoke(it) }
            }
        }
    }
}