package vn.edu.hust.soict.sv.dungnt.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CameraListActivity : AppCompatActivity(), CameraAdapter.OnCameraClickListener {
    private lateinit var imageViewCamera: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_view)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCameraList)
        imageViewCamera = findViewById(R.id.imageViewCamera)

        val cameras: List<CameraModel> = listOf(
            CameraModel("Camera 1"),
            CameraModel("Camera 2"),
            CameraModel("Camera 3")
            // Thêm thông tin cho các camera khác nếu cần
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = CameraAdapter(this, cameras)
        recyclerView.adapter = adapter

        adapter.setOnCameraClickListener(this)
    }

    override fun onCameraClicked(camera: CameraModel) {
        val imageUrl = "https://images.pexels.com/photos/460961/pexels-photo-460961.jpeg"

        val intent = Intent(this, CameraImageActivity::class.java)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }
}
