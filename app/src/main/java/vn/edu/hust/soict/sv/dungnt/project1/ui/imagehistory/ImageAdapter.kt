package vn.edu.hust.soict.sv.dungnt.project1.ui.imagehistory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import vn.edu.hust.soict.sv.dungnt.project1.R
import vn.edu.hust.soict.sv.dungnt.project1.data.model.ImageModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageAdapter(private var images: List<ImageModel>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        val imageUrl = "http://103.176.178.96:8000/api/v1/photo/${image.imageID}"

        Glide.with(holder.itemView)
            .load(imageUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.baseline_error_24)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)
        holder.textViewTime.text = formatDateToString(image.createdAt)
    }

    fun formatDateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(date)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun updateImages(newImages: List<ImageModel>) {
        images = newImages.sortedByDescending { it.createdAt }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTime: TextView = itemView.findViewById(R.id.textViewTime)

        init {
            imageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val image = images[position]
                    val imageUrl = "http://103.176.178.96:8000/api/v1/photo/${image.imageID}"
                    showImageDialog(
                        itemView.context,
                        imageUrl
                    )
                }
            }
        }
    }

    private fun showImageDialog(context: Context, imageUrl: String) {
        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_image, null)
        val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)

        Log.d("ImageAdapter", "DialogView: $dialogView, DialogImageView: $dialogImageView")

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.baseline_error_24)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dialogImageView)

        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()
    }

}
