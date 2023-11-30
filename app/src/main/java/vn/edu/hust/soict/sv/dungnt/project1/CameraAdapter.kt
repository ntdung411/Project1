package vn.edu.hust.soict.sv.dungnt.project1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CameraAdapter(private val context: Context, private val cameraList: List<CameraModel>) :
    RecyclerView.Adapter<CameraAdapter.CameraViewHolder>() {

    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cameraName: TextView = itemView.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CameraViewHolder(view)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        val camera = cameraList[position]
        holder.cameraName.text = camera.cameraName

        holder.itemView.setOnClickListener {
            onCameraClickListener?.onCameraClicked(camera)
        }
    }

    override fun getItemCount(): Int {
        return cameraList.size
    }

    interface OnCameraClickListener {
        fun onCameraClicked(camera: CameraModel)
    }

    private var onCameraClickListener: OnCameraClickListener? = null

    fun setOnCameraClickListener(listener: OnCameraClickListener) {
        this.onCameraClickListener = listener
    }
}
