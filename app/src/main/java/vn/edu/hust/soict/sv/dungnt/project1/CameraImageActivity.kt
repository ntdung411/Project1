package vn.edu.hust.soict.sv.dungnt.project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso

class CameraImageActivity : AppCompatActivity() {
    private lateinit var imageViewCamera: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_image)

        imageViewCamera = findViewById(R.id.imageViewCamera)

        val imageUrl = intent.getStringExtra("imageUrl")

        Picasso.get().load(imageUrl).into(imageViewCamera)
    }
}