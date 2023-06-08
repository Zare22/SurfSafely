package hr.algebra.surfsafely.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.surfsafely.R
import hr.algebra.surfsafely.databinding.ItemImageBinding
import hr.algebra.surfsafely.dto.quiz.QuizDto

class ImageRecycleAdapter(private val images: MutableMap<Long?, Bitmap?>) : RecyclerView.Adapter<ImageRecycleAdapter.ImageViewHolder>() {

    var selectedId: Long? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val bitmap = images.values.elementAt(position)
        val id = images.keys.elementAt(position)
        holder.bind(bitmap, id)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmap: Bitmap?, id: Long?) {
            binding.imageView.setImageBitmap(bitmap)
            if (id == selectedId) {
                binding.imageView.setBackgroundResource(R.drawable.selected_image_border)
            } else {
                binding.imageView.setBackgroundResource(0)
            }

            binding.imageView.setOnClickListener {
                val previousSelectedId = selectedId
                selectedId = id
                notifyItemChanged(images.keys.indexOf(previousSelectedId))
                notifyItemChanged(adapterPosition)
            }
        }
    }
}
