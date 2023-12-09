import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.edu.hust.soict.sv.dungnt.project1.CameraModel
import vn.edu.hust.soict.sv.dungnt.project1.R

class CameraAdapter(
    private val context: Context,
    private val cameraList: List<CameraModel>,
    private val onCameraClickListener: OnCameraClickListener
) : RecyclerView.Adapter<CameraAdapter.CameraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_camera, parent, false)
        return CameraViewHolder(view)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        val camera = cameraList[position]
        holder.bind(camera)

        holder.itemView.setOnClickListener {
            onCameraClickListener.onCameraClicked(camera)
        }
    }

    override fun getItemCount(): Int {
        return cameraList.size
    }

    interface OnCameraClickListener {
        fun onCameraClicked(camera: CameraModel)
        fun onRenameCameraClicked(camera: CameraModel)
        fun onDeleteCameraClicked(camera: CameraModel)
    }

    inner class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cameraName: TextView = itemView.findViewById(R.id.textViewCameraName)
        fun bind(camera: CameraModel) {
            cameraName.text = camera.cameraName
            // Cập nhật các thông tin khác của camera nếu cần thiết
        }

        init {
            val optionsButton: ImageButton = itemView.findViewById(R.id.imageButtonOptions)

            optionsButton.setOnClickListener {
                showPopupMenu(optionsButton, adapterPosition)
            }
        }

        private fun showPopupMenu(view: View, position: Int) {
            val popupMenu = PopupMenu(context, view)
            popupMenu.menuInflater.inflate(R.menu.popup_menu_camera_options, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_rename_camera -> {
                        onCameraClickListener.onRenameCameraClicked(cameraList[position])
                        true
                    }
                    R.id.menu_delete_camera -> {
                        onCameraClickListener.onDeleteCameraClicked(cameraList[position])
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }
}
