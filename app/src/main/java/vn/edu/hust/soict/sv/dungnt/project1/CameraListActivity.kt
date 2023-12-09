package vn.edu.hust.soict.sv.dungnt.project1

import CameraAdapter
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CameraListActivity : AppCompatActivity(), CameraAdapter.OnCameraClickListener {
    private lateinit var imageViewCamera: ImageView
    private lateinit var btnAddCamera: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CameraAdapter
    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val cameras: MutableList<CameraModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_list)

        recyclerView = findViewById(R.id.recyclerViewCameraList)
        imageViewCamera = findViewById(R.id.imageViewCamera)
        btnAddCamera = findViewById(R.id.btnAddCamera)

        setupFirestoreListener()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CameraAdapter(this, cameras, this)
        recyclerView.adapter = adapter

        btnAddCamera.setOnClickListener {
            showAddCameraDialog()
        }
    }

    private fun setupFirestoreListener() {
        database.collection("cameras")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                snapshots?.let { snapshot ->
                    val updatedCameras = mutableListOf<CameraModel>()

                    for (document in snapshot) {
                        val camera = document.toObject(CameraModel::class.java)
                        updatedCameras.add(camera)
                    }

                    cameras.clear()
                    cameras.addAll(updatedCameras)

                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
    }

    override fun onCameraClicked(camera: CameraModel) {
        val imageUrl = "https://images.pexels.com/photos/460961/pexels-photo-460961.jpeg"

        val intent = Intent(this, CameraImageActivity::class.java)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }

    override fun onRenameCameraClicked(camera: CameraModel) {
        showRenameCameraDialog(camera)
    }

    override fun onDeleteCameraClicked(camera: CameraModel) {
        showDeleteCameraDialog(camera)
    }

    private fun addCameraToFirestore(cameraName: String) {
        val newCamera = CameraModel(cameraName)
        val newCameraId = generateCameraId()

        newCamera.id = newCameraId

        database.collection("cameras")
            .document(newCameraId)
            .set(newCamera)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
            }
    }

    private fun generateCameraId(): String {
        return UUID.randomUUID().toString()
    }

    private fun updateCameraNameInFirestore(camera: CameraModel, newName: String) {
        val updateData = hashMapOf(
            "cameraName" to newName,
            "id" to camera.id
        )

        database.collection("cameras").document(camera.id)
            .set(updateData)
            .addOnSuccessListener {
                val index = cameras.indexOf(camera)
                if (index != -1) {
                    cameras[index].cameraName = newName
                    recyclerView.adapter?.notifyItemChanged(index)
                }
            }
            .addOnFailureListener { e ->
            }
    }


    private fun deleteCameraFromFirestore(camera: CameraModel) {
        val docRef = database.collection("cameras").document(camera.id)

        if (camera.id.isNotEmpty()) {
            docRef.delete()
                .addOnSuccessListener {
                    val index = cameras.indexOf(camera)
                    if (index != -1) {
                        cameras.removeAt(index)
                        recyclerView.adapter?.notifyItemRemoved(index)
                    }
                }
                .addOnFailureListener { e ->
                }
        }
    }


    private fun showAddCameraDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Camera")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val cameraName = input.text.toString()
            addCameraToFirestore(cameraName)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }


    private fun showRenameCameraDialog(camera: CameraModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Rename Camera")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(camera.cameraName)
        input.setSelection(input.text.length)
        builder.setView(input)

        builder.setPositiveButton("Update") { dialog, _ ->
            val newName = input.text.toString()
            updateCameraNameInFirestore(camera, newName)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun showDeleteCameraDialog(camera: CameraModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Camera")
        builder.setMessage("Are you sure you want to delete this camera?")

        builder.setPositiveButton("Delete") { dialog, _ ->
            deleteCameraFromFirestore(camera)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
