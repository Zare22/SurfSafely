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

class ImageRecycleAdapter(private val images: List<Bitmap>) : RecyclerView.Adapter<ImageRecycleAdapter.ImageViewHolder>() {

    var selectedImage: Bitmap? = null
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Bitmap) {
            binding.imageView.setImageBitmap(image)
            if (image == selectedImage) {
                binding.imageView.setBackgroundResource(R.drawable.selected_image_border)
            } else {
                binding.imageView.setBackgroundResource(0)
            }

            binding.imageView.setOnClickListener {
                val previousSelectedImage = selectedImage
                selectedImage = image
                notifyItemChanged(images.indexOf(previousSelectedImage))
                notifyItemChanged(adapterPosition)
            }
        }
    }
}
